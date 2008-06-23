
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
    
    std::string javafx_classpath_libs;
    std::string javafxc_bootclasspath_libs;
    std::string javafxc_classpath_libs;
    std::string javafxdoc_bootclasspath_libs;
    
    Configuration();
    ~Configuration();
    
    int initConfiguration (int argc, char** argv);
    std::string evaluatePath (std::string& libs);
    
private:
    void init();
    void readConfigFile();
    int parseArgs(int argc, char** argv);
    int fileExists(const std::string& path);
};

#endif	/* _CONFIGURATION_H */

