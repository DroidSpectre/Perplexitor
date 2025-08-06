package perplexity.editor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
	
	private EditText editText;
	private Button newButton, saveButton, openButton;
	
	private static final int CREATE_FILE_REQUEST_CODE = 1;
	private static final int OPEN_FILE_REQUEST_CODE = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Initialize UI elements
		editText = findViewById(R.id.editText);
		newButton = findViewById(R.id.newButton);
		saveButton = findViewById(R.id.saveButton);
		openButton = findViewById(R.id.openButton);
		
		// Request storage permissions at runtime (if not already granted)
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
		}
		
		// New button clears the text editor
		newButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				editText.setText("");
				Toast.makeText(MainActivity.this, "New document created", Toast.LENGTH_SHORT).show();
			}
		});
		
		// Save button saves the text to a file
		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showSaveDialog();
			}
		});
		
		// Open button loads text from a file
		openButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showOpenDialog();
			}
		});
	}
	
	private void showSaveDialog() {
		Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TITLE, "myfile.txt");
		startActivityForResult(intent, CREATE_FILE_REQUEST_CODE);
	}
	
	private void showOpenDialog() {
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("text/plain");
		startActivityForResult(intent, OPEN_FILE_REQUEST_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && data != null) {
			Uri uri = data.getData();
			if (requestCode == CREATE_FILE_REQUEST_CODE) {
				saveFile(uri);
				} else if (requestCode == OPEN_FILE_REQUEST_CODE) {
				openFile(uri);
			}
		}
	}
	
	private void saveFile(Uri uri) {
		String text = editText.getText().toString();
		
		if (TextUtils.isEmpty(text)) {
			Toast.makeText(this, "Cannot save empty document", Toast.LENGTH_SHORT).show();
			return;
		}
		
		try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
			if (outputStream != null) {
				outputStream.write(text.getBytes());
				outputStream.flush();
				Toast.makeText(this, "File saved to " + uri.toString(), Toast.LENGTH_LONG).show();
				} else {
				Toast.makeText(this, "Failed to open output stream", Toast.LENGTH_SHORT).show();
			}
			} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "Failed to save file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	private void openFile(Uri uri) {
		try (InputStream inputStream = getContentResolver().openInputStream(uri);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line).append("\n");
			}
			
			editText.setText(stringBuilder.toString());
			Toast.makeText(this, "File loaded", Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "Failed to open file", Toast.LENGTH_SHORT).show();
		}
	}
}