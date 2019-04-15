#include <iostream>

#include "simpleterminal.h"

SimpleTerminal::SimpleTerminal()
{

}

SimpleTerminal::SimpleTerminal(char * pszPrompt)
{
	this->pszPrompt = pszPrompt;
}
