package Ninja.coder.Ghostemane.code.databin;

import Ninja.coder.Ghostemane.code.R;
import Ninja.coder.Ghostemane.code.interfaces.FileCallBack;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileMaker {
  protected Context context;
  protected FileCallBack call;
  protected File file;

  public FileMaker(Context context) {
    this.context = context;
  }

  public void setFolderName(String path) {
    AlertDialog dialog =
        new MaterialAlertDialogBuilder(context)
            .setTitle("Create a new file")
            .setPositiveButton(android.R.string.ok, (ccc, rieo) -> {})
            .setNegativeButton(android.R.string.cancel, null)
            .setView(R.layout.makefolder)
            .create();
    dialog.setOnShowListener(
        __ -> {
          EditText et = dialog.findViewById(R.id.editor);
          TextInputLayout input = dialog.findViewById(R.id.top);
          var posbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
          if (et.getText().toString().isEmpty()) {
            posbutton.setEnabled(false);
          } else posbutton.setEnabled(true);

          et.setHint(path);
          et.addTextChangedListener(
              new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence c, int arg1, int arg2, int arg3) {
                  
                  if (c.toString().isEmpty()) {
                    posbutton.setEnabled(false);
                    input.setErrorEnabled(false);
                  } else posbutton.setEnabled(true);

                  file = new File(path, c.toString());
                  if (file.exists()) {
                    input.setErrorEnabled(true);
                    input.setError("This file already exists");
                    posbutton.setEnabled(false);
                  } else {
                    input.setErrorEnabled(false);
                    posbutton.setEnabled(true);
                  }
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

                @Override
                public void afterTextChanged(Editable arg0) {}
              });
          posbutton.setOnClickListener(
              ___ -> {
                // make file
                try {
                  String fileName = et.getText().toString();
                  file = new File(path, fileName);
                  if (!file.exists()) {
                    file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file);
                    String sampleText = "This is a sample text for the new file.";
                    fos.write(sampleText.getBytes());
                    fos.close();

                    Toast.makeText(et.getContext(), "فایل با موفقیت ایجاد شد", Toast.LENGTH_SHORT)
                        .show();
                  } else {
                    input.setErrorEnabled(true);
                    input.setError("This file already exists");
                  }
                  input.setErrorEnabled(false);
                  posbutton.setEnabled(true);
                  call.onDoneMakeFile("");
                  dialog.dismiss();
                } catch (IOException err) {
                  call.onError(err.getMessage());
                }
              });
        });
    if (dialog != null) {
      dialog.show();
    }
  }

  public void setCallBack(FileCallBack call) {
    this.call = call;
  }
}
