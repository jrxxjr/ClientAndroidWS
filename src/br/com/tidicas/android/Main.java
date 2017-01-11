package br.com.tidicas.android;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 
 * @author Evaldo Junior
 *
 */
public class Main extends Activity implements OnClickListener {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		findViewById(R.id.btnRest).setOnClickListener(this);
		findViewById(R.id.btnLimpar).setOnClickListener(this);
		findViewById(R.id.btnSoap).setOnClickListener(this);
		
		final Button btnRest = (Button)findViewById(R.id.btnRest);
		final Button btnSoap = (Button)findViewById(R.id.btnSoap);
		btnRest.setOnClickListener(this);
		btnSoap.setOnClickListener(this);
		EditText editRest = (EditText)findViewById(R.id.editRest);
   	 	EditText editSoap = (EditText)findViewById(R.id.editSoap);
		editRest.setText("");
		editSoap.setText("");
	}
	
	@Override
	public void onStart() {
		super.onStart();
		EditText editRest = (EditText)findViewById(R.id.editRest);
   	 	EditText editSoap = (EditText)findViewById(R.id.editSoap);
		editRest.setText("");
		editSoap.setText("");
	}

	@Override
	public void onClick(View arg0) {
		final Button btnRest = (Button)findViewById(R.id.btnRest);
		final Button btnLimpar = (Button)findViewById(R.id.btnLimpar);
		final Button btnSoap = (Button)findViewById(R.id.btnSoap);
				
	        switch (arg0.getId()) {
		        case R.id.btnSoap: {
		        	btnSoap.setClickable(false);
		        	new LongRunningGetSoap().execute();
		            break;	            
		        }
		           
		        case R.id.btnRest: {
		        	 btnRest.setClickable(false);
		        	 new LongRunningGetRest().execute();
		             break;
		        }

	    }
		
		btnLimpar.setOnClickListener(new View.OnClickListener() {
		     @Override
		     public void onClick(View v) {
		    	 EditText editRest = (EditText)findViewById(R.id.editRest);
		    	 EditText editSoap = (EditText)findViewById(R.id.editSoap);
				 editRest.setText("");
				 editSoap.setText("");
	
		     }
		});
	
	}

	private class LongRunningGetRest extends AsyncTask <Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {

			final StringBuilder result = new StringBuilder();
				
			 List<CadastroTo> tos= RestFulClient.returnServico();

			 if (tos!=null && tos.size()>0){
				 
				for (CadastroTo cadastroTo : tos) {
					result.append("Id:" + cadastroTo.getCadastroId());
					result.append("\r\n");
					result.append("Quantidade:" + cadastroTo.getQuantidade());
					result.append("\r\n");
				}
				
			}			 			
			    	 
			return result.toString();
		}
	
		@SuppressLint("NewApi")
		protected void onPostExecute(String results) {
					
			final Button btnRest = (Button)findViewById(R.id.btnRest);
			
			if (results!=null){
				
				EditText et = (EditText)findViewById(R.id.editRest);
				et.setText(results);
				btnRest.setClickable(true);
			}
						
		}
	

	}
		
		private class LongRunningGetSoap extends AsyncTask <Void, Void, String> {
			
			@Override
			protected String doInBackground(Void... params) {
							
				StringBuilder result = new StringBuilder();
		        	
		        CadastroTo to = SoapClient.returnServico();
				
		    	 if (to!=null){
			    	 								
						result.append("Id:" + to.getCadastroId());
						result.append("\r\n");
						result.append("Quantidade:" + to.getQuantidade());
						result.append("\r\n");
						
				}
		        			
				return result.toString();
			}
		
			@SuppressLint("NewApi")
			protected void onPostExecute(String results) {
				final Button btnSoap = (Button)findViewById(R.id.btnSoap);		
				EditText editSoap = (EditText)findViewById(R.id.editSoap);
				editSoap.setText(results);
				btnSoap.setClickable(true);
				
							
			}
		
		}

}	