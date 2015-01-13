#include "PluginDefinition.h"
#include "menuCmdID.h"

FuncItem funcItem[nbFunc];
NppData nppData;

void pluginInit(HANDLE hModule) {

}

void pluginCleanUp() {

}

void commandMenuInit() {
    setCommand(0, TEXT("Info"), Info, NULL, false);
}

void commandMenuCleanUp() {

}

bool setCommand(size_t index, TCHAR *cmdName, PFUNCPLUGINCMD pFunc, ShortcutKey *sk, bool check0nInit)  {
	if(index >= nbFunc) {
		return false;
	}
	
	if(!pFunc) {
		return false;
	}

    lstrcpy(funcItem[index]._itemName, cmdName);
    funcItem[index]._pFunc = pFunc;
    funcItem[index]._init2Check = check0nInit;
    funcItem[index]._pShKey = sk;

    return true;
}

void Info() {
    ::MessageBox(NULL, TEXT("MyChannel-Apps.de"), TEXT("Info"), MB_OK);
}
