/**
 * CSPRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package iptv;

public interface CSPRequest extends java.rmi.Remote {
    public iptv.CSPResult execCmd(java.lang.String CSPID, java.lang.String LSPID, java.lang.String correlateID, java.lang.String cmdFileURL) throws java.rmi.RemoteException;
}
