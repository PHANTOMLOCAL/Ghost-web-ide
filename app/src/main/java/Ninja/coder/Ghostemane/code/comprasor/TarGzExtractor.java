package Ninja.coder.Ghostemane.code.comprasor;

import Ninja.coder.Ghostemane.code.tasks.AsyncTaskCompat;
import Ninja.coder.Ghostemane.code.tasks.app.ProgressDialogCompat;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import com.google.android.material.shape.*;
import android.util.Log;
import android.view.View;
import org.apache.commons.compress.archivers.tar.*;
import org.apache.commons.compress.archivers.sevenz.*;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FileUtils;
import java.io.*;

public class TarGzExtractor {
	private Context context;
	private OnExtractionListener mListener;
	
	public TarGzExtractor(Context context, OnExtractionListener mListener) {
		this.context = context;
		this.mListener = mListener;
	}
	
	public void extract(File inputFile, File outputDirectory) {
		new ExtractTask(inputFile, outputDirectory).execute();
	}
	
	protected class ExtractTask extends AsyncTaskCompat<Void, Object, Void> {
		private File mInputFile;
		private File mOutputDirectory;
		private long mTotalSize;
		
		ExtractTask(File inputFile, File outputDirectory) {
			mInputFile = inputFile;
			mOutputDirectory = outputDirectory;
			mTotalSize = mInputFile.length();
		}
		
		ProgressDialogCompat mProgressDialog = new ProgressDialogCompat(context, ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		MaterialShapeDrawable shap = new MaterialShapeDrawable(
				ShapeAppearanceModel.builder().setAllCorners(CornerFamily.CUT, 17f).build());
		shap.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FFFCB07D")));
		shap.setStrokeWidth(2f);
		shap.setFillColor(ColorStateList.valueOf(Color.parseColor("#FF281D1B")));
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setTitle("Extracting file " + mInputFile.getAbsoluteFile());
			mProgressDialog.setMessage("Please wait.");
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.getWindow().getDecorView().setBackground(shap);
			mProgressDialog.setCancelable(false);
			mProgressDialog.setMax(100);
			mProgressDialog.setProgress(0);
			mProgressDialog.show();
			View view = mProgressDialog.getWindow().getDecorView();
			view.setScaleX(0f);
			view.setScaleY(0f);
			ObjectAnimator alertAnim = new ObjectAnimator();
			ObjectAnimator alertAnim1 = new ObjectAnimator();
			alertAnim.setTarget(view);
			alertAnim.setPropertyName("scaleX");
			alertAnim.setFloatValues((float) (1));
			alertAnim.setDuration((int) (250));
			alertAnim.start();
			alertAnim1.setTarget(view);
			alertAnim1.setPropertyName("scaleY");
			alertAnim1.setFloatValues((float) (1));
			alertAnim1.setDuration((int) (250));
			alertAnim1.start();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			try (FileInputStream fis = new FileInputStream(mInputFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			GzipCompressorInputStream gis = new GzipCompressorInputStream(bis);
			TarArchiveInputStream tis = new TarArchiveInputStream(gis)) {
				TarArchiveEntry entry;
				while ((entry = tis.getNextTarEntry()) != null) {
					File outputFile = new File(mOutputDirectory, entry.getName());
					if (entry.isDirectory()) {
						outputFile.mkdirs();
					} else {
						outputFile.getParentFile().mkdirs();
						try (OutputStream fos = FileUtils.openOutputStream(outputFile)) {
							byte[] buffer = new byte[4096];
							int length;
							long bytesRead = 0;
							while ((length = tis.read(buffer)) != -1) {
								fos.write(buffer, 0, length);
								bytesRead += length;
								int progress = (int) ((bytesRead * 100) / mTotalSize);
								publishProgress(progress, entry.getName());
								
							}
						}
					}
				}
			} catch (IOException e) {
				// Handle IO exception
				throw new RuntimeException(e.toString());
			}
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Object... values) {
			super.onProgressUpdate(values);
			int progress = (int) values[0];
			String name = "";
			if (values.length >= 2) {
				name = values[1].toString();
				// use 'name' variable
			} else {
				Log.e("MyApp", "Error: 'values' array does not have at least two elements");
				// or Toast.makeText(getApplicationContext(), "Error: 'values' array does not have at least two elements", Toast.LENGTH_LONG).show();
			}
			mProgressDialog.setProgress(progress);
			mProgressDialog.setMessage("Extracting " + name);
		}
		
		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			mProgressDialog.dismiss();
			mListener.onExtractionComplete();
		}
	}
	
	public interface OnExtractionListener {
		void onExtractionComplete();
	}
	
}
