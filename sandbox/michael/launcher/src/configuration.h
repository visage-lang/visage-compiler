
#ifndef _CONFIGURATION_H
#define	_CONFIGURATION_H

#include <string>

class Configuration {
public:
    std::string javacmd;
    std::string javafxpath;
    std::string classpath;
    std::string vmargs;
    std::string fxargs;
    
    Configuration();
    ~Configuration();
    
    int getConfiguration (int argc, char** argv);
    
private:
    void init();
    int parseArgs(int argc, char** argv);
    void setDefaults();
    int fileExists(const std::string& path);
};

#endif	/* _CONFIGURATION_H */

