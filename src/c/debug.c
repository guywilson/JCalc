#include <stdio.h>
#include <stdarg.h>

#include "debug.h"

FILE *	pipe;

void initRemoteLogging(char * pszPipeName)
{
	pipe = fopen(pszPipeName, "w");

	if (pipe == NULL) {
		printf("Failed to open pipe [%s]\n", pszPipeName);
	}
}

void finishRemoteLogging()
{
	fclose(pipe);
}

void remoteDebugChar(int ch)
{
	fputc(ch, pipe);
}

void remoteDebugLine(const char * pszDebugStr, ...)
{
	va_list		argptr;

	va_start(argptr, pszDebugStr);

	vfprintf(pipe, pszDebugStr, argptr);
	fputs("\n", pipe);
	fflush(pipe);

	va_end(argptr);
}
