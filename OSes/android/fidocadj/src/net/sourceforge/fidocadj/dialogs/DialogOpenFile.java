package net.sourceforge.fidocadj.dialogs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fidocadj.FidoEditor;
import net.sourceforge.fidocadj.R;
import android.app.Activity;
import android.app.Dialog;  
import android.app.DialogFragment;
import android.os.Bundle;  
import android.view.View;  
import android.view.Window;   
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
	<pre>
	This file is part of FidoCadJ.

    FidoCadJ is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FidoCadJ is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with FidoCadJ.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2014 by Dante Loi
 	</pre>
  @author Dante Loi
 
 */
public class DialogOpenFile extends DialogFragment 
{  
	private String[] files;
	private FidoEditor drawingPanel;
	
	@Override  
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{  
		final Activity context = getActivity();
		final Dialog dialog = new Dialog(context);
		
		drawingPanel = (FidoEditor)context.findViewById(R.id.drawingPanel);
		
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.open_file);
		
		File dir = context.getFilesDir();
		files = dir.list();
		
		List<String> listFiles = new ArrayList<String>();
		
		for( String str:files)
			if( str.indexOf("state.fcd.tmp") == -1 )
				listFiles.add(str);
		
		if(listFiles.isEmpty())
			listFiles.add("No such file.");
			
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                 context, 
                 R.layout.list_item, R.id.lblListItem,
                 listFiles);
		
		ListView list = (ListView) dialog.findViewById(R.id.fileList);
		list.setAdapter(arrayAdapter);
		list.setPadding(10, 10, 10, 10);
		
		OnItemClickListener clickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) 
			{
				
				StringBuilder text = new StringBuilder();
				text.append("[FIDOCAD]\n");
				File file = new File(context.getFilesDir(),files[position]);
				try {
				    BufferedReader br = new BufferedReader(
				    	new FileReader(file));
				    String line;

				    while ((line = br.readLine()) != null) {
				        text.append(line);
				        text.append('\n');
				    }
				    br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				drawingPanel.getParserActions().openFileName = files[position];
				drawingPanel.getParserActions().parseString(new 
					StringBuffer(text.toString()));
				drawingPanel.getUndoActions().saveUndoState();
				drawingPanel.invalidate();
				
				dialog.dismiss();		
			}
		};
		list.setOnItemClickListener(clickListener);

		return dialog;
	}
}  





