package com.sun.javafx.ideaplugin.debug;

import com.intellij.debugger.NoDataException;
import com.intellij.debugger.PositionManager;
import com.intellij.debugger.SourcePosition;
import com.intellij.debugger.engine.DebugProcess;
import com.intellij.debugger.engine.DebugProcessImpl;
import com.intellij.debugger.requests.ClassPrepareRequestor;
import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.request.ClassPrepareRequest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author David Kaspar
*/
public class FxPositionManager implements PositionManager {

    private final DebugProcess debugProcess;

    public FxPositionManager (DebugProcess debugProcess) {
        this.debugProcess = debugProcess;
    }

    @Nullable public SourcePosition getSourcePosition (Location location) throws NoDataException {
        if (location == null)
            throw new NoDataException ();
//        FxPlugin.FX_LANGUAGE.getParserDefinition ().createFile ()
//        SourcePosition.createFromLine ();
        return null; // TODO
    }

    @NotNull public List<ReferenceType> getAllClasses (SourcePosition classPosition) throws NoDataException {
// TODO - search source paths for relative path
//        String path = null;
//        for (PsiFile root : classPosition.getFile ().getPsiRoots ()) {
//            path = VfsUtil.getRelativePath (classPosition.getFile ().getVirtualFile (), root.getVirtualFile (), '/');
//            if (path != null)
//                break;
//        }
//        if (path == null)
//            throw new NoDataException ();
//        System.out.println ("path = " + path);
        String className = classPosition.getFile ().getVirtualFile ().getNameWithoutExtension (); // TODO
        return debugProcess.getVirtualMachineProxy ().classesByName (className);
    }

    @NotNull public List<Location> locationsOfLine (ReferenceType type, SourcePosition position) throws NoDataException {
        try {
            // TODO - resolve type in case of inner classes or instances
            int line = position.getLine() + 1;
            List<Location> locations;
            if (debugProcess.getVirtualMachineProxy ().versionHigher ("1.4"))
                locations = type.locationsOfLine (DebugProcessImpl.JAVA_STRATUM, null, line);
            else
                locations = type.locationsOfLine (line);
            if (locations == null  ||  locations.isEmpty())
                throw new NoDataException();
            return locations;
        } catch (AbsentInformationException e) {
            e.printStackTrace (); // TODO
            throw new NoDataException ();
        }
    }

    @Nullable public ClassPrepareRequest createPrepareRequest (ClassPrepareRequestor requestor, SourcePosition position) throws NoDataException {
//        System.out.println ("position.getFile () = " + position.getFile ());
//        System.out.println ("position.getFile ().findElementAt (position.getOffset ()) = " + position.getFile ().findElementAt (position.getOffset ()));
        return debugProcess.getRequestsManager ().createClassPrepareRequest (requestor, position.getFile ().getVirtualFile ().getNameWithoutExtension ()); // TODO
    }

}
