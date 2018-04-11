/**
 * CtmsSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package iptv;

import com.ai.injection.ResultNotify;

public class CtmsSoapBindingImpl implements iptv.CSPResponse {
    public CSPResult resultNotify(String CSPID, String LSPID, String CorrelateID, int CmdResult, String ResultFileURL) throws java.rmi.RemoteException {
        return ResultNotify.resultNotify(CSPID, LSPID, CorrelateID, CmdResult, ResultFileURL);
    }

}
