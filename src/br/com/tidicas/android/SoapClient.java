package br.com.tidicas.android;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class SoapClient {

	private static final String NAMESPACE = "http://localhost:8080/test";
    private static final String METHOD_NAME = "returnServico";
    private static final String SOAP_ACTION = "returnServico";
    private static String url = "http://192.168.1.30:8080/test/TesteJavaEndPointService?wsdl";
 
    public static CadastroTo returnServico() {
        CadastroTo cadastroTo = null;
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        
        PropertyInfo propParametro1 = new PropertyInfo();
        propParametro1.name = "parametro1";
        propParametro1.type = PropertyInfo.STRING_CLASS;
        propParametro1.setValue("A");
        request.addProperty(propParametro1);
        
        PropertyInfo propParametro2 = new PropertyInfo();
        propParametro2.name = "parametro2";
        propParametro2.type = PropertyInfo.STRING_CLASS;
        propParametro2.setValue("B");
        request.addProperty(propParametro2);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);        
        envelope.setOutputSoapObject(request);
        envelope.dotNet = false;
        HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
        
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
            int propsNum = resultsRequestSOAP.getPropertyCount();
            cadastroTo = new CadastroTo();
            
            for (int i = 0; i < propsNum; i++) {
            	SoapObject element = (SoapObject) resultsRequestSOAP.getProperty(i);
                cadastroTo.setCadastroId(Long.parseLong(element.getPropertyAsString("cadastroId")));
                cadastroTo.setQuantidade(Integer.parseInt(element.getPropertyAsString("quantidade")));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return cadastroTo;
    }
 
}
