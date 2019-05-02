#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#include "calc.h"

int main(int argc, char **argv)
{
	char szCmd[CMD_BUFFER_SIZE];

	memset(szCmd, 0, CMD_BUFFER_SIZE);

    strcpy(szCmd, "java -classpath ");
    strcat(szCmd, CLASS_PATH);
    strcat(szCmd, " com.guy.calc.Main");

    system(szCmd);
}
