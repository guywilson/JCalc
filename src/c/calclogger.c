#include <stdio.h>

int main(int argc, char **argv)
{
	FILE *	pipe;
	int		go = 1;
	int		ch = 0;

	pipe = fopen(argv[1], "r");

	if (pipe == NULL) {
		printf("Failed to open pipe [%s]\n", argv[1]);
	}

	while (go) {
		ch = fgetc(pipe);
		putchar(ch);

		if (ch == 'q' || ch == 'Q') {
			printf("\n\n");
			go = 0;
		}
	}

	fclose(pipe);
}
