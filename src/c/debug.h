#ifndef _INCL_DEBUG
#define _INCL_DEBUG

void initRemoteLogging();
void finishRemoteLogging();
void remoteDebugChar(int ch);
void remoteDebugLine(const char * pszDebugStr, ...);

#endif
