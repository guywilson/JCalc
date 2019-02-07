#include <stdlib.h>

#include "cmdhistory.h"

int ID = 0;

ListNode::ListNode(void * pData)
{
	this->data = pData;
	this->id = ID++;

	this->next = NULL;
	this->previous = NULL;
}

void * ListNode::getData()
{
	return data;
}

int ListNode::getID()
{
	return id;
}


LinkedList::LinkedList()
{
	head = NULL;
	tail = NULL;

	itemCount = 0;
}

void LinkedList::add(void * pData)
{
	add(new ListNode(pData));
}

void LinkedList::add(ListNode * n)
{
	if (head == NULL) {
		head = n;
		tail = n;

		n->previous = NULL;
		n->next = NULL;
	}
	else {
		tail->next = n;

		n->previous = tail;
		n->next = NULL;

		tail = n;
	}
}

void LinkedList::insert(int insertAfterID, void * pData)
{
	insert(insertAfterID, new ListNode(pData));
}

void LinkedList::insert(int insertAfterID, ListNode * n)
{
	ListNode *	current = head;

	while (current->next != NULL) {
		if (current->getID() == insertAfterID) {
			/*
			 * Found the right item...
			 */
			n->previous = current;
			n->next = current->next;

			current->next->previous = n;
			current->next = n;

			break;
		}
		else {
			/*
			 * To-do - throw exception
			 */
		}

		current = current->next;
	}
}

void LinkedList::remove(int id)
{
	ListNode * current = head;

	while (current->next != NULL) {
		if (current->getID() == id) {
			/*
			 * Found the right item...
			 */
			current->next->previous = current->previous;
			current->previous->next = current->next;

			delete current;

			break;
		}
		else {
			/*
			 * To-do - throw exception
			 */
		}

		current = current->next;
	}
}
