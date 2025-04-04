package mvc;

import java.io.Serializable;

public abstract class Model extends Publisher implements Serializable {
    private String fileName = null;
    private boolean unsavedChanges = false;
    

    public void changed() {
      unsavedChanges = true;
      notifySubscribers();
    }

    public void setUnsavedChanges(boolean b) {
      unsavedChanges = b;
    }

    public boolean getUnsavedChanges() {
        return unsavedChanges;
    }

    public void setFileName(String fName) {
      fileName = fName;
    }

    public String getFileName() {
        return fileName;
    }
}
