#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <stdarg.h>
#include <termios.h>

#include "calc.h"
#include "debug.h"


/*
 * Globals...
 */
char			szCmd[CMD_BUFFER_SIZE];
char			szCalculation[CMD_BUFFER_SIZE - 128];
char			szTemp[CMD_BUFFER_SIZE - 128];

int				cursorPos = 0;
int				eolPos = 0;

int				cmdBufferLength = 0;

PCMDBUFFERLIST	pCmdBufferList = NULL;


void decCursorPos()
{
	if (cursorPos > 0) {
		cursorPos--;
	}
}

void decEOLPos()
{
	if (eolPos > 0) {
		eolPos--;
	}
}

void incCursorPos()
{
	if (cursorPos < eolPos) {
		cursorPos++;
	}
}

void decCursorPosn(int n)
{
	int i = 0;

	for (i = 0;i < n;i++) {
		decCursorPos();
	}
}

void incCursorPosn(int n)
{
	int i = 0;

	for (i = 0;i < n;i++) {
		incCursorPos();
	}
}

int readch()
{
    struct termios oldt;
    struct termios newt;
    int ch;
    int fd_stdin;

    fd_stdin = fileno(stdin);
    
    tcgetattr(fd_stdin, &oldt);
    newt = oldt;
    newt.c_lflag &= ~(ICANON | ECHO);

    tcsetattr(fd_stdin, TCSANOW, &newt);

    ch = getchar();

    tcsetattr(fd_stdin, TCSANOW, &oldt);

    return ch;
}

void cursorLeft(int n)
{
	int i = 0;

	if (n < 0) {
		n = 0;
	}

	if (n > 0) {
		for (i = 0;i < n;i++) {
			putchar(CHAR_ESCAPE);
			putchar(CHAR_CSI);
			putchar(CHAR_LEFT);
		}
	}
}

void cursorRight(int n)
{
	int i = 0;

	if (n < 0) {
		n = 0;
	}

	if (n > 0) {
		for (i = 0;i < n;i++) {
			putchar(CHAR_ESCAPE);
			putchar(CHAR_CSI);
			putchar(CHAR_RIGHT);
		}
	}
}

void delete()
{
	int i = 0;
	int	cursorLeftCount = 0;

	cursorLeft(1);

	if (cursorPos < eolPos) {
		for (i = cursorPos;i < eolPos;i++) {
			putchar(szCalculation[i]);
			cursorLeftCount++;
		}

		putchar(' ');
		cursorLeftCount++;
	}

	// Terminate the string prior to the cursor...
	szCalculation[cursorPos - 1] = 0;

	if (cursorPos < eolPos) {
		strcpy(szTemp, &szCalculation[cursorPos]);
		strcat(szCalculation, szTemp);
	}
	else {
		putchar(' ');
		cursorLeft(1);
	}

	cursorLeft(cursorLeftCount);
	decCursorPos();

	if (eolPos > 0) {
		eolPos--;
	}
}

void insert(int ch)
{
	int i = 0;
	int j = 0;
	int	cursorLeftCount = 0;

	if (cursorPos < eolPos) {
		strcpy(szTemp, &szCalculation[cursorPos]);
	}

	szCalculation[cursorPos] = ch;
	putchar(ch);

	if (cursorPos < eolPos) {
		for (i = cursorPos + 1,j = 0;i <= eolPos;i++,j++) {
			putchar(szTemp[j]);
			szCalculation[i] = szTemp[j];

			cursorLeftCount++;
		}

		szCalculation[i] = 0;

		cursorLeft(cursorLeftCount);
	}

	eolPos++;
	cursorPos++;
}

void clearLine()
{
	putchar(CHAR_ESCAPE);
	putchar(CHAR_CSI);
	putchar('2');
	putchar(CHAR_ERASEINLINE);

	cursorLeft(cursorPos);

	cursorPos = 0;
	eolPos = 0;
}

void debugf(const char * pszString, ...)
{
	char		szBuffer[256];
	va_list		argptr;

	va_start(argptr, pszString);
	vsprintf(szBuffer, pszString, argptr);
	va_end(argptr);

	printf("%s", szBuffer);

	eolPos += strlen(szBuffer);
	incCursorPosn(strlen(szBuffer));
}

void runCalculation(char * calculation)
{
	memset(szCmd, 0, CMD_BUFFER_SIZE);

    strcpy(szCmd, "java -classpath ");
    strcat(szCmd, CLASS_PATH);
    strcat(szCmd, " com.guy.calc.Main \"");
    strcat(szCmd, calculation);
    strcat(szCmd, "\"");

    system(szCmd);
}

void addCmd(char * pszCmd)
{
	if (pCmdBufferList == NULL) {
		pCmdBufferList = (PCMDBUFFERLIST)malloc(sizeof(CMDBUFFERLIST));

		if (pCmdBufferList == NULL) {
			debugf("Failed to allocate command buffer\n\n");
			exit(-1);
		}

		pCmdBufferList->pNext = NULL;
		pCmdBufferList->pPrevious = NULL;
	}
	else {
		if (cmdBufferLength == MAX_CMD_BUFFER_DEPTH) {
			/*
			 * Find the oldest command to free up...
			 */
			while (pCmdBufferList->pPrevious != NULL) {
				pCmdBufferList = pCmdBufferList->pPrevious;
			}

			/*
			 * Unlink this item...
			 */
			free(pCmdBufferList->pItem->pszCommand);
			free(pCmdBufferList->pItem);

			pCmdBufferList = pCmdBufferList->pNext;
			free(pCmdBufferList->pPrevious);
			pCmdBufferList->pPrevious = NULL;

			/*
			 * Traverse back to the bottom (the latest command)...
			 */
			while (pCmdBufferList->pNext != NULL) {
				pCmdBufferList = pCmdBufferList->pNext;
			}
		}

		pCmdBufferList->pNext = (PCMDBUFFERLIST)malloc(sizeof(CMDBUFFERLIST));

		if (pCmdBufferList->pNext == NULL) {
			debugf("Failed to allocate command buffer\n\n");
			exit(-1);
		}

		pCmdBufferList->pNext->pPrevious = pCmdBufferList;
		pCmdBufferList = pCmdBufferList->pNext;

		pCmdBufferList->pNext = NULL;
	}

	pCmdBufferList->pItem = (PCMDBUFFERITEM)malloc(sizeof(CMDBUFFERITEM));

	if (pCmdBufferList->pItem == NULL) {
		debugf("Failed to allocate command buffer item\n\n");
		exit(-1);
	}

	pCmdBufferList->pItem->pszCommand = (char *)malloc(strlen(pszCmd) + 1);

	if (pCmdBufferList->pItem->pszCommand == NULL) {
		debugf("Failed to allocate command string\n\n");
		exit(-1);
	}

	strcpy(pCmdBufferList->pItem->pszCommand, pszCmd);
	pCmdBufferList->pItem->length = strlen(pszCmd);

	cmdBufferLength++;
}

char * getNextCmd()
{
	if (pCmdBufferList->pNext != NULL) {
		pCmdBufferList = pCmdBufferList->pNext;
	}

	return pCmdBufferList->pItem->pszCommand;
}

char * getPreviousCmd()
{
	if (pCmdBufferList->pPrevious != NULL) {
		pCmdBufferList = pCmdBufferList->pPrevious;
	}

	return pCmdBufferList->pItem->pszCommand;
}

void handleCSI()
{
	int		ch;

	ch = readch();

	switch (ch) {
		case CHAR_UP:
			clearLine();
			strcpy(szCalculation, getPreviousCmd());
			debugf(szCalculation);
			break;

		case CHAR_DOWN:
			clearLine();
			strcpy(szCalculation, getNextCmd());
			debugf(szCalculation);
			break;

		case CHAR_LEFT:
			cursorLeft(1);
			decCursorPos();
			break;

		case CHAR_RIGHT:
			cursorRight(1);
			incCursorPos();
			break;

		default:
			debugf("CSI sequence [0x%02X] not currently supported.\n\n", ch);
			break;
	}
}

void handleEscapeChar()
{
	int		ch;

	ch = readch();

	switch (ch) {
		case CHAR_CSI:
			handleCSI();
			break;

		default:
			debugf("Escape sequence [0x%02X] not currently supported.\n\n", ch);
			break;
	}
}

int main(int argc, char **argv)
{
    int		ch = 0;
    int 	go = 1;

    //initRemoteLogging(argv[1]);

	memset(szCalculation, 0, CMD_BUFFER_SIZE - 128);
	memset(szTemp, 0, CMD_BUFFER_SIZE - 128);

	puts("Welcome to Calc. A command line scientific calculator.");
	puts("Type a calculation or command at the prompt, type 'help' for info.\n");

    while (go) {
        ch = readch();

        switch (ch) {
			case CHAR_ESCAPE:
				handleEscapeChar();
				break;

			case CHAR_NEWLINE:
				szCalculation[eolPos] = 0;

				putchar(CHAR_NEWLINE);

				addCmd(szCalculation);
				//debugf("Calculation: %s\n", szCalculation);
				runCalculation(szCalculation);

			    cursorPos = 0;
			    eolPos = 0;

			    memset(szCalculation, 0, CMD_BUFFER_SIZE - 128);
				memset(szTemp, 0, CMD_BUFFER_SIZE - 128);
				break;

			case CHAR_DELETE:
				delete();
				break;

			case 'q':
			case 'Q':
				go = 0;
//				remoteDebugChar(ch);
				break;

			default:
				if (ch < ' ' || ch == 127) {
					debugf("[0x%02X]", ch);
				}
				else {
					insert(ch);
				}
				break;
        }
    }

    /*
     * Head to the top of the list...
     */
    while (pCmdBufferList->pPrevious != NULL) {
    	pCmdBufferList = pCmdBufferList->pPrevious;
    }

    /*
     * Traverse the list, freeing up each item...
     */
    while (pCmdBufferList->pNext != NULL) {
    	/*
    	 * Unlink this item...
    	 */
    	free(pCmdBufferList->pItem->pszCommand);
    	free(pCmdBufferList->pItem);

    	pCmdBufferList = pCmdBufferList->pNext;
    	free(pCmdBufferList->pPrevious);
    	pCmdBufferList->pPrevious = NULL;
    }

    //finishRemoteLogging();
}
