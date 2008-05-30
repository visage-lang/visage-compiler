package com.sun.javafx.ideaplugin.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.RunConfigurationExtension;
import com.intellij.execution.configurations.*;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.runners.RunnerInfo;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.ProjectJdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.vfs.VirtualFile;
import org.jdom.Element;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author David Kaspar
 */
public class FxRunConfiguration extends ModuleBasedConfiguration {

    private static final String MAIN_CLASS_PROPERTY = "main-class";
    private static final String VM_PARAMETERS_PROPERTY = "vm-parameters";
    private static final String PROGRAM_PARAMETERS_PROPERTY = "program-parameters";
    private static final String WORKING_DIRECTORY_PROPERTY = "working-directory";

    private final Map<Class<? extends RunConfigurationExtension>, Object> extensionSettings = new HashMap<Class<? extends RunConfigurationExtension>, Object> ();

    private String mainClass;
    private String vmParameters;
    private String programParameters;
    private String workingDirectory;

    public FxRunConfiguration (String name, ConfigurationFactory configurationFactory, Project project) {
        super (name, new RunConfigurationModule(project, true), configurationFactory);
    }

    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor () {
        return new FxRunSettingsEditor (getProject ());
    }

    public ConfigurationType getType () {
        return FxRunConfigurationType.getInstance ();
    }

    @Nullable
    public Object getExtensionSettings (Class<? extends RunConfigurationExtension> extensionClass) {
        return extensionSettings.get (extensionClass);
    }

    public void setExtensionSettings (Class<? extends RunConfigurationExtension> extensionClass, Object value) {
        extensionSettings.put (extensionClass, extensionSettings);
    }

    public RunProfileState getState (DataContext context, RunnerInfo runnerInfo, RunnerSettings runnerSettings, ConfigurationPerRunnerSettings configurationSettings) throws ExecutionException {
        final Module module = getConfigurationModule ().getModule ();
        if (module == null)
            throw new ExecutionException ("No module set");
        ModuleRootManager manager = ModuleRootManager.getInstance (module);
        final ProjectJdk jdk = manager.getJdk ();
        if (jdk == null)
            throw new ExecutionException ("No JDK set");
        if (mainClass == null  ||  "".equals (mainClass))
            throw new ExecutionException ("No Main-Class set");

        CommandLineState state = new CommandLineState (runnerSettings, configurationSettings) {
            protected GeneralCommandLine createCommandLine () throws ExecutionException {
                JavaParameters params = new JavaParameters ();
                params.configureByModule (module, JavaParameters.CLASSES_ONLY);
                if (workingDirectory != null  &&  ! "".equals (workingDirectory))
                    params.setWorkingDirectory (workingDirectory);
                else
                    params.setWorkingDirectory (getProject ().getBaseDir ().getPath ());
                if (vmParameters != null)
                    params.getVMParametersList ().addParametersString (vmParameters);
                if (programParameters != null)
                    params.getProgramParametersList ().addParametersString (programParameters);
                params.setMainClass (mainClass);
                params.setJdk (jdk);
                return GeneralCommandLine.createFromJavaParameters (params);
            }
        };
        TextConsoleBuilder builder = TextConsoleBuilderFactory.getInstance().createBuilder(getProject ());
        state.setConsoleBuilder(builder);
        state.setModulesToCompile (ModuleRootManager.getInstance (getConfigurationModule ().getModule ()).getDependencies ());
        return state;
    }

    public void checkConfiguration () throws RuntimeConfigurationException {
        if (mainClass == null  &&  "".equals (mainClass))
            throw new RuntimeConfigurationException ("No main JavaFX Script class specified");
    }

    public Module[] getModules () {
        return ModuleManager.getInstance (getProject ()).getModules ();
    }

    public Collection<Module> getValidModules () {
        ArrayList<Module> list = new ArrayList<Module>();
        for (Module module : getModules ())
            if (module.getModuleType() instanceof JavaModuleType)
                list.add(module);
        return list;
    }

    public void readExternal (Element element) throws InvalidDataException {
        super.readExternal (element);
        mainClass = element.getAttributeValue (MAIN_CLASS_PROPERTY);
        vmParameters = element.getAttributeValue (VM_PARAMETERS_PROPERTY);
        programParameters = element.getAttributeValue (PROGRAM_PARAMETERS_PROPERTY);
        workingDirectory = element.getAttributeValue (WORKING_DIRECTORY_PROPERTY);
        if (workingDirectory == null) {
            Project project = getProject ();
            if (project != null) {
                VirtualFile baseDir = project.getBaseDir ();
                if (baseDir != null)
                    workingDirectory = baseDir.getPath ();
            }
        }
        getConfigurationModule ().readExternal (element);
    }

    public void writeExternal (Element element) throws WriteExternalException {
        super.writeExternal (element);
        if (mainClass != null)
            element.setAttribute (MAIN_CLASS_PROPERTY, mainClass);
        if (vmParameters != null  &&  ! "".equals (vmParameters))
            element.setAttribute (VM_PARAMETERS_PROPERTY, vmParameters);
        if (programParameters != null  &&  ! "".equals (programParameters))
            element.setAttribute (PROGRAM_PARAMETERS_PROPERTY, programParameters);
        if (workingDirectory != null  &&  ! "".equals (workingDirectory))
            element.setAttribute (WORKING_DIRECTORY_PROPERTY, workingDirectory);
        getConfigurationModule ().writeExternal (element);
    }

    protected ModuleBasedConfiguration createInstance () {
        return new FxRunConfiguration (getName (), getFactory (), getProject ());
    }

    public String getMainClass () {
        return mainClass;
    }

    public void setMainClass (String mainClass) {
        this.mainClass = mainClass;
    }

    public String getVmParameters () {
        return vmParameters;
    }

    public void setVmParameters (String vmParameters) {
        this.vmParameters = vmParameters;
    }

    public String getProgramParameters () {
        return programParameters;
    }

    public void setProgramParameters (String programParameters) {
        this.programParameters = programParameters;
    }

    public String getWorkingDirectory () {
        return workingDirectory;
    }

    public void setWorkingDirectory (String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

}
