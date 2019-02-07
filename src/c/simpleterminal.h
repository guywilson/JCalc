#include "cmdhistory.h"
#include "cursor.h"

#ifndef _INCL_SIMPLETERMINAL
#define _INCL_SIMPLETERMINAL

class SimpleTerminal
{
private:
	Cursor	cursor;
	int		eolPos;
	int		promptLength();

	char *	pszCommandBuffer;
	char *	pszPrompt;

	void incEOL();
	void decEOL();

	int readch();

	void handleEscape();
	void handleCSI();

	Command getPreviousCmd();
	Command getNextCommand();
	void	addCommand();

	void leftArrow();
	void rightArrow();
	void upArrow();
	void downArrow();

	void append();
	void insert();
	void backspace();

protected:
	void handleUpArrow();
	void handleDownArrow();
	void handleLeftArrow();
	void handleRightArrow();
	void handleBackspace();

	void handleEnter();

public:
	SimpleTerminal();
	SimpleTerminal(char * pszPrompt);

	~SimpleTerminal();

	void setPrompt(char * pszPrompt);
	void showPrompt();

	void clearLine();

	int getEOLPos();

	Cursor getCursor();

	int getch();
};

#endif
