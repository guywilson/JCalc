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
char	szCmd[CMD_BUFFER_SIZE];
char	szCalculation[CMD_BUFFER_SIZE - 128];
char	szTemp[CMD_BUFFER_SIZE - 128];

int		cursorPos = 0;
int		eolPos = 0;


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

int main(int argc, char **argv)
{
    int		ch = 0;
    int 	ch2 = 0;
    int 	ch3 = 0;
    int 	go = 1;

    //initRemoteLogging(argv[1]);

	memset(szCalculation, 0, CMD_BUFFER_SIZE - 128);
	memset(szTemp, 0, CMD_BUFFER_SIZE - 128);

    while (go) {
        ch = readch();

        switch (ch) {
			case CHAR_ESCAPE:
				ch2 = readch();

				if (ch2 == CHAR_CSI) {
					ch3 = readch();

					switch (ch3) {
						case CHAR_UP:
							clearLine();
							debugf("[UP]");
							break;

						case CHAR_DOWN:
							clearLine();
							debugf("[DOWN]");
							break;

						case CHAR_LEFT:
							cursorLeft(1);
							decCursorPos();
							break;

						case CHAR_RIGHT:
							cursorRight(1);
							incCursorPos();
							break;
					}
				}
				break;

			case CHAR_NEWLINE:
				szCalculation[eolPos] = 0;

				putchar(CHAR_NEWLINE);

				debugf("Calculation: %s\n", szCalculation);
				//runCalculation(szCalculation);

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

    //finishRemoteLogging();
}
