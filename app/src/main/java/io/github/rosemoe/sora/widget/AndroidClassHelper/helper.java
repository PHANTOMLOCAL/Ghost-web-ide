package io.github.rosemoe.sora.widget.AndroidClassHelper;

import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.blankj.utilcode.util.ClipboardUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.shape.ShapeAppearanceModel;
import io.github.rosemoe.sora.widget.CodeEditor;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class helper {
	private CodeEditor editor;
	String clip = "";

	public helper(CodeEditor editor) {
		this.editor = editor;
	}

	public helper BindList() {
		MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(editor.getContext());
		dialogBuilder.setTitle("Class Viewer");
		dialogBuilder.setMessage("See All Class Android how to User ?");
		ListView listView = new ListView(editor.getContext());
		listView.setLayoutParams(
				new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT));
		//	dialogBuilder.setView(listView);

		if (editor.getCursor().isSelected()) {

			android.content.ClipboardManager clipboard = (ClipboardManager) editor.getContext()
					.getSystemService(editor.getContext().CLIPBOARD_SERVICE);
			String clip = clipboard.getText().toString();
			clip = editor.getText().toString();

			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(editor.getContext(),
					android.R.layout.simple_list_item_1, getClassM(clip)) {

			};
			

			listView.setAdapter(arrayAdapter);
			((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
			listView.setOnItemClickListener((parant, view, pos, id) -> {
				String text = listView.getItemAtPosition(pos).toString();
				ClipboardUtils.copyText(getClassM(text).get(pos));
			});
		} else {
			Dialogerror("Error", "select to item like in android.view.View end see All method ");
		}

		dialogBuilder.setPositiveButton("ok", null);

		dialogBuilder.show();
		return this;
	}

	protected ArrayList<String> getClassM(String name) {
		ArrayList<String> listData = new ArrayList<>();
		try {
			Class clazz = Class.forName(name);
			if (clazz != null) {
				for (Method method : clazz.getDeclaredMethods()) {
					listData.add(method.toString());
				}
			}
		} catch (Exception d) {
			Dialogerror("Error ", d.getMessage());
		}
		return listData;
	}

	protected helper Dialogerror(String title, String msg) {
		final MaterialAlertDialogBuilder dialogBuilders = new MaterialAlertDialogBuilder(editor.getContext());
		dialogBuilders.setTitle(title);
		dialogBuilders.setMessage(msg);
		dialogBuilders.setNegativeButton("ok", null);
		dialogBuilders.setBackground(shapeDrawable());
		dialogBuilders.show();
		return this;
	}

	protected MaterialShapeDrawable shapeDrawable() {
		MaterialShapeDrawable materialShapeUtils = new MaterialShapeDrawable(
				ShapeAppearanceModel.builder().setAllCorners(CornerFamily.CUT, 20f).build());
		materialShapeUtils.setFillColor(ColorStateList.valueOf(Color.BLACK));
		materialShapeUtils.setStroke(1, Color.RED);
		return materialShapeUtils;
	}

}