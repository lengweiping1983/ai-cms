/**
 * CSPResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package iptv;

public interface CSPResponse extends java.rmi.Remote {
    public iptv.CSPResult resultNotify(java.lang.String CSPID, java.lang.String LSPID, java.lang.String correlateID, int cmdResult, java.lang.String resultFileURL) throws java.rmi.RemoteException;
}
