#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <stdarg.h>
#include <termios.h>

#include "calc.h"


/*
 * Globals...
 */
char	szCmd[CMD_BUFFER_SIZE];
char	szCalculation[CMD_BUFFER_SIZE - 128];
char	szTemp[CMD_BUFFER_SIZE - 128];

int		cursorPos = 0;
int		eolPos = 0;

FILE *	pipe;

void debugLine(const char * pszDebugStr, ...)
{
	va_list		argptr;

	va_start(argptr, pszDebugStr);

	vfprintf(pipe, pszDebugStr, argptr);
	fputs("\n", pipe);
	fflush(pipe);

	va_end(argptr);
}

void decCursorPos()
{
	if (cursorPos > 0) {
		cursorPos--;
	}

	debugLine("[%d]", cursorPos);
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

	debugLine("[%d]", cursorPos);
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

//    debugLine("readch() enter - Cursor pos [%d]", cursorPos);

    fd_stdin = fileno(stdin);
    
    tcgetattr(fd_stdin, &oldt);
    newt = oldt;
    newt.c_lflag &= ~(ICANON | ECHO);

    tcsetattr(fd_stdin, TCSANOW, &newt);

    ch = getchar();

    tcsetattr(fd_stdin, TCSANOW, &oldt);

//    debugLine("readch() exit - Cursor pos [%d]", cursorPos);
    
    return ch;
}

void clearLine()
{
	putchar(CHAR_ESCAPE);
	putchar(CHAR_CSI);
	putchar('2');
	putchar(CHAR_ERASEINLINE);

	cursorPos = 0;
}

void cursorLeft(int n)
{
	if (n > 9) {
		n = 9;
	}
	else if (n < 0) {
		n = 0;
	}

	if (n > 0) {
		putchar(CHAR_ESCAPE);
		putchar(CHAR_CSI);
		putchar('0' + n);
		putchar(CHAR_LEFT);
	}
}

void cursorRight(int n)
{
	if (n > 9) {
		n = 9;
	}
	else if (n < 0) {
		n = 0;
	}

	if (n > 0) {
		putchar(CHAR_ESCAPE);
		putchar(CHAR_CSI);
		putchar('0' + n);
		putchar(CHAR_RIGHT);
	}
}

void delete()
{
	int i = 0;
	int	cursorLeftCount = 0;

//    debugLine("delete() enter - Cursor pos [%d]", cursorPos);

	putchar(CHAR_ESCAPE);
	putchar(CHAR_CSI);
	putchar(CHAR_LEFT);

	if (cursorPos < eolPos) {
		for (i = cursorPos;i < eolPos;i++) {
			debugLine("['%c'][%d][%d][%d]", szCalculation[i], i, cursorPos, eolPos);

			putchar(szCalculation[i]);

			cursorLeftCount++;
		}

		putchar(' ');
		cursorLeftCount++;
	}

	szCalculation[cursorPos - 1] = 0;

	if (cursorPos < (eolPos - 1)) {
		strcpy(szTemp, &szCalculation[cursorPos]);
		strcat(szCalculation, szTemp);

		debugLine("Concat string [%s]", szTemp);
	}
	else {
		putchar(' ');

		putchar(CHAR_ESCAPE);
		putchar(CHAR_CSI);
		putchar(CHAR_LEFT);
	}

	cursorLeft(cursorLeftCount);
	decCursorPos();

	if (eolPos > 0) {
		eolPos--;
	}

//    debugLine("delete() exit - Cursor pos [%d]", cursorPos);
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
			debugLine("['%c'][%d][%d][%d]", szCalculation[i], i, cursorPos, eolPos);

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

    pipe = fopen(argv[1], "w");

	if (pipe == NULL) {
		printf("Failed to open pipe [%s]\n", argv[1]);
	}

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
							printf("[UP]");
							break;

						case CHAR_DOWN:
							clearLine();
							printf("[DOWN]");
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

			    printf("Calculation: %s\n", szCalculation);
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
				fputc(ch, pipe);
				break;

			default:
				if (ch < ' ' || ch == 127) {
					printf("[0x%02X]", ch);
				}
				else {
					insert(ch);
				}
				break;
        }
    }

    fclose(pipe);
}
