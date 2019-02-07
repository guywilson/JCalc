#ifndef _INCL_CMDHISTORY
#define _INCL_CMDHISTORY

class ListNode
{
private:
	void *		data;
	int			id;

public:
	ListNode(void * p);

	ListNode *	next;
	ListNode *	previous;

	void *		getData();
	int			getID();
};

class LinkedList
{
private:
	ListNode *	head;
	ListNode *	tail;

	int			itemCount;

public:
	LinkedList();

	void		add(void * pData);
	void		add(ListNode * n);

	void		insert(int insertAfterID, void * pData);
	void		insert(int insertAfterID, ListNode * n);

	void		remove(int id);

	ListNode *	getNode();
};

class Command
{
private:
	char * 		pszCmdString;
	int 		cmdLength;

public:
	Command();
	Command(char * pszCmd);

	int 		getLength();
	char * 		getCmdString();
};

#endif
