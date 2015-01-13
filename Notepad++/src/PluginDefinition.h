#ifndef PLUGINDEFINITION_H
	#define PLUGINDEFINITION_H

	#include "PluginInterface.h"

	const TCHAR NPP_PLUGIN_NAME[] = TEXT("KFramework");
	const int nbFunc = 1;

	void pluginInit(HANDLE hModule);
	void pluginCleanUp();
	void commandMenuInit();
	void commandMenuCleanUp();
	bool setCommand(size_t index, TCHAR *cmdName, PFUNCPLUGINCMD pFunc, ShortcutKey *sk = NULL, bool check0nInit = false);
	void Info();
#endif
