#ifndef _INCL_CURSOR
#define _INCL_CURSOR

class Cursor
{
private:
	int		cursorPos;

public:
	Cursor();

	operator ++();
	operator --();

	int		getCursorPos();
	void 	setCursorPos(int pos);

	void	moveCursor(int value);
};

#endif
