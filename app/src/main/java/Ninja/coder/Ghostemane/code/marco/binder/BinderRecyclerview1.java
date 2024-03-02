package Ninja.coder.Ghostemane.code.marco.binder;

import Ninja.coder.Ghostemane.code.ColorAndroid12;
import Ninja.coder.Ghostemane.code.FileUtil;
import Ninja.coder.Ghostemane.code.GlideUtilApi.GlideCompat;
import Ninja.coder.Ghostemane.code.MFileClass;
import Ninja.coder.Ghostemane.code.R;
import Ninja.coder.Ghostemane.code.SvgShow;
import Ninja.coder.Ghostemane.code.marco.BindViewListMarger;
import Ninja.coder.Ghostemane.code.marco.FileCounter;
import Ninja.coder.Ghostemane.code.marco.binder.bindchilder.ChilderRecyclerviewCompat;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.color.MaterialColors;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BinderRecyclerview1 {

  public static void bindz(
      ArrayList<HashMap<String, Object>> files,
      int _position,
      TextView textview2,
      ImageView imageview1,
      TextView folderName) {
    var token = files.get(_position).get("path").toString();
    var context = imageview1.getContext();
    folderName.setTextColor(MaterialColors.getColor(folderName,ColorAndroid12.colorOnSurface));
    
    if (FileUtil.isDirectory(token)) {
      BindViewListMarger.run(imageview1, folderName);
      var fileR = new FileCounter(textview2);
      fileR.execute(token);
      //  ColorAndroid12.setColorFilter(imageview1);
    } else {
      BindViewListMarger.runfromFile(folderName);
      if (token.endsWith(".html")) {
        SvgShow.GetAsster(context, imageview1, "file_type_html.svg");
        getTime(token, textview2);
      } else if (token.endsWith(".css")) {
        getTime(token, textview2);
        SvgShow.GetAsster(context, imageview1, "file_type_css.svg");
      } else if (token.endsWith(".js")) {
        getTime(token, textview2);
        SvgShow.GetAsster(context, imageview1, "file_type_node.svg");
      } else if (token.endsWith(".svg")) {
        getTime(token, textview2);
        GlideCompat.LoadSvg(token, imageview1);
      } else if (token.endsWith(".py")) {
        getTime(token, textview2);
        SvgShow.GetAsster(context, imageview1, "file_type_python.svg");
      } else if (token.endsWith(".jar")) {
        getTime(token, textview2);
        SvgShow.GetAsster(context, imageview1, "file_type_jar.svg");
      } else if (token.endsWith(".json")) {
        getTime(token, textview2);
        SvgShow.GetAsster(context, imageview1, "file_type_json2.svg");
      } else if (token.endsWith(".java")) {
        getTime(token, textview2);
        SvgShow.GetAsster(context, imageview1, "file_type_java.svg");
      } else if (token.endsWith(".cpp")) {
        getTime(token, textview2);
        SvgShow.GetAsster(context, imageview1, "file_type_cpp3.svg");
      } else if (token.endsWith(".c")) {
        getTime(token, textview2);
        SvgShow.GetAsster(context, imageview1, "file_type_c3.svg");
      } else if (token.endsWith(".cs")) {
        getTime(token, textview2);
        SvgShow.GetAsster(context, imageview1, "file_type_csharp2.svg");
      } else if (token.endsWith(".ghost")) {
        getTime(token, textview2);
        SvgShow.GetAsster(context, imageview1, "file_type_ink.svg");
      } else if (token.endsWith(".xml")) {
        getTime(token, textview2);
        GlideCompat.LoadVector(token, imageview1);
      } else if (token.endsWith(".ninja")) {
        getTime(token, textview2);
        SvgShow.GetAsster(context, imageview1, "file_type_ninja.svg");
      } else if (token.endsWith(".sh")) {
        getTime(token, textview2);
        SvgShow.GetAsster(context, imageview1, "rowbash.svg");
      } else if (ChilderRecyclerviewCompat.getChildAudio(token)) {
        getTime(token, textview2);
        GlideCompat.GlideLoadMp3(imageview1, token);
      } else if (TaskVideo(token)) {
        getTime(token, textview2);
        GlideCompat.LoadMp4(imageview1, token);
      } else if (token.endsWith(".php")) {
        getTime(token, textview2);
        imageview1.setImageResource(R.drawable.languagephp);
      } else if (token.endsWith(".apk")) {
        getTime(token, textview2);
        try {
          android.content.pm.PackageManager packageManager = context.getPackageManager();
          android.content.pm.PackageInfo packageInfo =
              packageManager.getPackageArchiveInfo(token, 0);
          packageInfo.applicationInfo.sourceDir = token;
          packageInfo.applicationInfo.publicSourceDir = token;
          imageview1.setImageDrawable(packageInfo.applicationInfo.loadIcon(packageManager));
          packageInfo = null;
          packageManager = null;
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else if (token.endsWith(".go")) {
        getTime(token, textview2);
        imageview1.setImageResource(R.drawable.languagego);
      } else if (token.endsWith(".txt")) {
        getTime(token, textview2);
        imageview1.setImageResource(R.drawable.textfile);
      } else if (token.endsWith(".pdf")) {
        getTime(token, textview2);
        try {
          File file = new File(token);
          GlideCompat.loadImgPdf(file, imageview1);
        } catch (IOException err) {
          err.printStackTrace();
        }
      } else if (archiveView(token)) {
        getTime(token, textview2);
        imageview1.setImageResource(R.drawable.zipandtar);
      } else if (PhotoView(token)) {
        getTime(token, textview2);
        GlideCompat.LoadImage(imageview1, token, textview2);
      } else if (token.endsWith(".dart")) {
        getTime(token, textview2);
        SvgShow.GetAsster(context, imageview1, "dart.svg");
      } else if (token.endsWith(".swift")) {
        getTime(token, textview2);
        SvgShow.GetAsster(context, imageview1, "swift.svg");
      } else if (token.endsWith(".ttf") || token.endsWith(".otf")) {
        getTime(token, textview2);
        imageview1.setImageResource(R.drawable.fontlookimageview);
      } else if (token.endsWith(".aa")) {
        getTime(token, textview2);
        imageview1.setImageResource(R.drawable.resulttheme);
      } else if (token.endsWith(".gradle") || token.endsWith(".groovy")) {
        getTime(token, textview2);
        imageview1.setImageResource(R.drawable.gradle);
      } else if (token.endsWith(".smali")) {
        getTime(token, textview2);
        imageview1.setImageResource(R.drawable.smashingmagazine);
      } else if (token.endsWith(".g4")) {
        getTime(token, textview2);
        imageview1.setImageResource(R.drawable.ant4);
      } else if (token.endsWith(".ts")) {
        getTime(token, textview2);
        imageview1.setImageResource(R.drawable.typescriptlang);
      } else if (token.endsWith(".kt")) {
        getTime(token, textview2);
        SvgShow.GetAsster(context, imageview1, "kotlin.svg");
      } else if (token.endsWith(".scss")) {
        getTime(token, textview2);
        SvgShow.GetAsster(context, imageview1, "file_type_scss2.svg");
      } else if (token.endsWith(".md")) {
        getTime(token, textview2);
        SvgShow.GetAsster(context, imageview1, "mdx.svg");
      } else if(token.endsWith(".sass")){
        getTime(token, textview2);
        SvgShow.GetAsster(context, imageview1, "file_type_sass.svg");
      }else{
        getTime(token, textview2);
        SvgShow.GetAsster(context, imageview1, "default_file.svg");
        off(imageview1);
      }
    }
  }

  private static void getTime(String path, TextView view) {
    try {

      if (view != null) {
        view.setText(
            MFileClass.convertBytes(FileUtil.getFileLength(path))
                .concat(", ".concat(MFileClass.getLastModifiedOfFile(path, "HH:mm,dd/MM/yyyy"))));
      }
    } catch (Exception err) {
      err.printStackTrace();
    }
  }

  private static boolean TaskVideo(String path) {
    return path.endsWith(".mp4")
        || path.endsWith(".mkv")
        || path.endsWith(".avi")
        || path.endsWith(".mov")
        || path.endsWith(".wmv")
        || path.endsWith(".flv")
        || path.endsWith("." + ChilderRecyclerviewCompat.getVideoFormat());
  }

  private static boolean archiveView(String path) {
    return path.endsWith(".tar")
        || path.endsWith(".rar")
        || path.endsWith(".zip")
        || path.endsWith(".tar.gz")
        || path.endsWith(".tar.xz")
        || path.endsWith(".7z");
  }

  private static boolean PhotoView(String path) {
    return (path.endsWith(".jpg")
        || path.endsWith(".jpeg")
        || path.endsWith(".png")
        || path.endsWith(".gif")
        || path.endsWith(".bmp")
        || path.endsWith(".webp"));
  }

  public static void off(ImageView img) {
    if (img != null) {
      img.setBackgroundColor(0);
      img.setColorFilter(null);
      img.setPadding(0, 0, 0, 0);
    }
  }
}
