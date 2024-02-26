package Ninja.coder.Ghostemane.code.layoutmanager;

import Ninja.coder.Ghostemane.code.ApplicationLoader;
import Ninja.coder.Ghostemane.code.R;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.github.pedrovgs.lynx.LynxConfig;
import com.github.pedrovgs.lynx.LynxShakeDetector;
import com.github.pedrovgs.lynx.LynxView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

public class LogCatBottomSheet {
  private AppCompatActivity context;
  private LynxView mLynxView;
  protected ImageView imageView1;
  private static final String LYNX_CONFIG_EXTRA = "extra_lynx_config";

  public LogCatBottomSheet(AppCompatActivity context) {
    this.context = context;
  }

  public void run() {
    View view = LayoutInflater.from(context).inflate(R.layout.custom_logcat, null, false);
    mLynxView = view.findViewById(R.id.mLynxView);
    imageView1 = view.findViewById(R.id.img_more_item_logcat);
    mLynxView.showItems(false);
    mLynxView.showSearcher(false);
    BottomSheetDialog dialog = new BottomSheetDialog(context);
    dialog.setContentView(view);
    runer();
    dialog.setCancelable(false);
    dialog.show();
    imageView1.setOnClickListener(
        ___ -> {
          var menu =
              new PowerMenu.Builder(context)
                  .setIsMaterial(true)
                  .addItem(new PowerMenuItem("Close"))
                  .addItem(new PowerMenuItem("Show Menu"))
                  .addItem(new PowerMenuItem("Clear"))
                  .build();
          menu.setAutoDismiss(true);
          menu.setAnimation(MenuAnimation.DROP_DOWN);
          menu.setShowBackground(false);
          menu.setOnMenuItemClickListener(
              (pos, __view__) -> {
                switch (pos) {
                  case 0 -> {
                    dialog.dismiss();
                    break;
                  }
                  case 1 -> {
                    mLynxView.showItems(true);
                    break;
                  }
                  case 2 -> {
                    mLynxView.post(() -> mLynxView.clear());
                    break;
                  }
                }
              });
          menu.showAsAnchorCenter(imageView1);
        });
  }

  private void runer() {
    LynxConfig lynxConfig = getLynxConfig();
    mLynxView.setLynxConfig(lynxConfig);
    _disableLynxShakeDetector();
  }

  private LynxConfig getLynxConfig() {
    Bundle extras = context.getIntent().getExtras();
    LynxConfig lynxConfig = new LynxConfig();
    if (extras != null && extras.containsKey(LYNX_CONFIG_EXTRA)) {
      lynxConfig = (LynxConfig) extras.getSerializable(LYNX_CONFIG_EXTRA);
    }
    return lynxConfig;
  }

  public void _enableLynxShakeDetector() {
    LynxShakeDetector.enable();
  }

  public void _disableLynxShakeDetector() {
    LynxShakeDetector.disable();
  }
}
