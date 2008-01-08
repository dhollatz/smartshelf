// Folgender ifdef-Block ist die Standardmethode zum Erstellen von Makros, die das Exportieren 
// aus einer DLL vereinfachen. Alle Dateien in dieser DLL werden mit dem SL2SERWRAPPER_EXPORTS-Symbol
// kompiliert, das in der Befehlszeile definiert wurde. Das Symbol darf nicht für ein Projekt definiert werden,
// das diese DLL verwendet. Alle anderen Projekte, deren Quelldateien diese Datei beinhalten, erkennen 
// SL2SERWRAPPER_API-Funktionen als aus einer DLL importiert, während die DLL
// mit diesem Makro definierte Symbole als exportiert ansieht.
#ifdef SL2SERWRAPPER_EXPORTS
#define SL2SERWRAPPER_API __declspec(dllexport)
#else
#define SL2SERWRAPPER_API __declspec(dllimport)
#endif

//extern SL2SERWRAPPER_API int nSL2SERWrapper;

SL2SERWRAPPER_API int returnBool(int);

SL2SERWRAPPER_API int init(char* ComStr, char* BaudStr);