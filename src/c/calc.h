#ifndef _INCL_CALC
#define _INCL_CALC

#define CLASS_PATH			"/Library/Java/Extensions/jcalc-1.0.0.jar"
//#define CLASS_PATH				"./target/jcalc-1.0.0.jar"

#define CMD_BUFFER_SIZE			4096

#define CHAR_ESCAPE				0x1B
#define CHAR_CSI				0x5B

#define CHAR_UP					0x41	// 'A'
#define CHAR_DOWN				0x42	// 'B'
#define CHAR_LEFT				0x44	// 'D'
#define CHAR_RIGHT				0x43	// 'C'

#define CHAR_BACKSPACE			0x08
#define CHAR_DELETE				0x7F

#define CHAR_NEWLINE			0x0A	// '\n'
#define CHAR_ERASEINLINE		0x4B	// 'K'

#define MAX_CMD_BUFFER_DEPTH	25


typedef struct _CMDBUFFERITEM
{
	char *				pszCommand;
	int					length;
}
CMDBUFFERITEM;

typedef CMDBUFFERITEM * PCMDBUFFERITEM;

typedef struct _CMDBUFFERLIST
{
	struct _CMDBUFFERLIST	*	pNext;
	struct _CMDBUFFERLIST	*	pPrevious;

	int							id;

	PCMDBUFFERITEM				pItem;
}
CMDBUFFERLIST;

typedef CMDBUFFERLIST * PCMDBUFFERLIST;

#endif
