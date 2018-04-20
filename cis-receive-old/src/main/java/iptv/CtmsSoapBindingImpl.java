/**
 * CtmsSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package iptv;

import com.ai.injection.ExecCmd;

public class CtmsSoapBindingImpl implements iptv.CSPRequest {
    public iptv.CSPResult execCmd(java.lang.String CSPID, java.lang.String LSPID, java.lang.String CorrelateID, java.lang.String CmdFileURL)
            throws java.rmi.RemoteException {
        return ExecCmd.execCmd(CSPID, LSPID, CorrelateID, CmdFileURL);
    }

}
