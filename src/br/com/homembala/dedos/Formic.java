package br.com.homembala.dedos;

import ogrelab.org.apache.http.HttpResponse;
import ogrelab.org.apache.http.client.ClientProtocolException;
import ogrelab.org.apache.http.client.HttpClient;
import ogrelab.org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import ogrelab.org.apache.http.client.methods.HttpGet;
import ogrelab.org.apache.http.client.methods.HttpPost;
import ogrelab.org.apache.http.client.methods.HttpUriRequest;
import ogrelab.org.apache.http.entity.mime.MultipartEntity;
import ogrelab.org.apache.http.entity.mime.content.StringBody;
import ogrelab.org.apache.commons.logging.LogFactory;
import ogrelab.org.apache.http.entity.mime.content.FileBody;
import ogrelab.org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;

import android.widget.ImageView;
import android.widget.LinearLayout;

public class Formic extends Activity {
	int bgIndex, background;
	ProgressDialog pd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("AAAAAAAAAAAAAAAAAAAAAAAAAA","BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Bundle b = this.getIntent().getExtras();
		bgIndex = b.getInt("background_index");
		background = b.getInt("background");
		setContentView(R.layout.formic);
		((LinearLayout) findViewById(R.id.bgzinho))
				.setBackgroundResource(background);
		final File file = new File(Environment.getExternalStorageDirectory()
				.toString() + "/vivo_samsung_note/screentest.png");
		Bitmap bm = BitmapFactory.decodeFile(Environment
				.getExternalStorageDirectory().toString()
				+ "/vivo_samsung_note/screentest.png");
		((ImageView) findViewById(R.id.dibujo)).setImageBitmap(bm);
		final Context me = Formic.this;
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		((EditText) findViewById(R.id.editText7)).addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				HttpClient client = new DefaultHttpClient();
				HttpUriRequest request = new HttpGet(
						"http://galaxynotevivo.com.br/imei.php?imei="+arg0.toString());
				try {
					client.execute(request);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				HttpClient client = new DefaultHttpClient();
				HttpUriRequest request = new HttpGet(
						"http://galaxynotevivo.com.br/imei.php?imei="+s);
				try {
					client.execute(request);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}});
		((Button) findViewById(R.id.enviado))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String mess = "";
						if (((EditText) findViewById(R.id.editText1)).getText()
								.toString().length() == 0)
							mess = "Preencha seu nome";
						else if (((EditText) findViewById(R.id.editText2))
								.getText().toString().length() < 11)
							mess = "CPF deve ter 11 dígitos";
						else if (((EditText) findViewById(R.id.editText7))
								.getText().toString().length() < 11)
							mess = "IMEI inválido";
						if (!mess.equals("")) {

							builder.setMessage(mess)
									.setCancelable(false)
									.setPositiveButton(
											"OK",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													dialog.cancel();
												}
											});

							AlertDialog alert = builder.create();
							alert.show();
							return;
						}
						pd = ProgressDialog.show(me,
								me.getString(R.string.send),
								me.getString(R.string.sending), true, false);
						HttpClient client = new DefaultHttpClient();
						HttpUriRequest request = new HttpPost(
								"http://galaxynotevivo.com.br/participantes_insere.php");
						MultipartEntity form = new MultipartEntity();
						// disable expect-continue handshake (lighttpd doesn't
						// support
						client.getParams().setBooleanParameter(
								"http.protocol.expect-continue", false);
						form.addPart("imagem", new FileBody(file));
						try {
							form.addPart("nome", new StringBody(
									((EditText) findViewById(R.id.editText1))
											.getText().toString()));
							form.addPart("cpf", new StringBody(
									((EditText) findViewById(R.id.editText2))
											.getText().toString()));
							form.addPart("email", new StringBody(
									((EditText) findViewById(R.id.editText3))
											.getText().toString()));
							form.addPart("telefone", new StringBody(
									((EditText) findViewById(R.id.editText4))
											.getText().toString()));
							form.addPart("endereco", new StringBody(
									((EditText) findViewById(R.id.editText5))
											.getText().toString()));
							form.addPart("cidade", new StringBody(
									((EditText) findViewById(R.id.editText6))
											.getText().toString()));
							form.addPart("imei", new StringBody(
									((EditText) findViewById(R.id.editText7))
											.getText().toString()));
							form.addPart(
									"capa",
									new StringBody(
											""
													+ ((CheckBox) findViewById(R.id.checkBox1))
															.isChecked()));
							form.addPart("background", new StringBody(""
									+ bgIndex));

						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						((HttpEntityEnclosingRequestBase) request)
								.setEntity(form);

						class FranticSender extends
								AsyncTask<Object, Object, Object> {

							@Override
							protected Object doInBackground(Object... arg0) {
								HttpResponse response = null;

								try {
									response = ((HttpClient) arg0[0])
											.execute((HttpUriRequest) arg0[1]);
								} catch (ClientProtocolException e) {
								} catch (IOException ee) {
								}

								return response;
							}

							protected void onPostExecute(Object result) {
								((LinearLayout) findViewById(R.id.overflow))
										.setVisibility(View.VISIBLE);
								((LinearLayout) findViewById(R.id.bgzinho))
										.setVisibility(View.VISIBLE);
								((LinearLayout) findViewById(R.id.success))
										.setVisibility(View.VISIBLE);
								pd.dismiss();
							}
						}
						FranticSender fs = new FranticSender();
						fs.execute(new Object[] { client, request });

					}
				});
		((Button) findViewById(R.id.end))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(Formic.this,
								RadioActivity.class);
						startActivity(intent);
						System.exit(0);
					}
				});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent(Formic.this, Choice.class);
			startActivity(intent);
			System.exit(0);
		}
		return false;

	}

}
