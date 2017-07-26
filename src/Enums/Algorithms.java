package Enums;

public enum Algorithms {
	Canonical				("http://www.w3.org/2006/12/xml-c14n11", "http://www.w3.org/2006/12/xml-c14n11"),
	CanonicalComm			("http://www.w3.org/2006/12/xml-c14n11#WithComments", "http://www.w3.org/2006/12/xml-c14n11#WithComments"),
	Canonicalization		("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", "http://www.w3.org/TR/2001/REC-xml-c14n-20010315"),
	CanonicalizationComm	("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments", "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments"),
	SignatureDSA1			("http://www.w3.org/2000/09/xmldsig#dsa-sha1", "DSA/ECB/NOPADDING"),
	SignatureDSA256			("http://www.w3.org/2000/xmldsig11#dsa-sha256", "DSA/ECB/NOPADDING"),
	SignatureRSA1			("http://www.w3.org/2000/09/xmldsig#rsa-sha1", "RSA/ECB/NOPADDING"),
	SignatureRSA256			("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256", "RSA/ECB/NOPADDING"),
	SignatureRSA384			("http://www.w3.org/2001/04/xmldsig-more#rsa-sha384", "RSA/ECB/NOPADDING"),
	SignatureRSA512			("http://www.w3.org/2001/04/xmldsig-more#rsa-sha512", "RSA/ECB/NOPADDING"),
	TransformXSLT			("http://www.w3.org/TR/1999/REC-xslt-19991116", "XSLT"),
	TransformXPath			("http://www.w3.org/TR/1999/REC-xpath-19991116", "XPath"),
	TransformEnveloped		("http://www.w3.org/2000/09/xmldsig#enveloped-signature", "Enveloped"),
	DigestSHA1				("http://www.w3.org/2000/09/xmldsig#sha1", "SHA-1"),
	DigestSHA256			("http://www.w3.org/2001/04/xmlenc#sha256", "SHA-256"),
	DigestSHA384			("http://www.w3.org/2001/04/xmldsig-more#sha384", "SHA-384"),
	DigestSHA512			("http://www.w3.org/2001/04/xmlenc#sha512", "SHA-512"),
//	HMAC1					("http://www.w3.org/2000/09/xmldsig#hmac-sha1"),
//	HMAC256					("http://www.w3.org/2001/04/xmldsig-more#hmac-sha256"),
//	HMAC384					("http://www.w3.org/2001/04/xmldsig-more#hmac-sha384"),
//	HMAC512					("http://www.w3.org/2001/04/xmldsig-more#hmac-sha512"),
	Encoding				("http://www.w3.org/2000/09/xmldsig#base64", "base64"),
	Charset					("utf-8", "utf-8"),
	X509					("ds:X509Data", "X.509");
	

	private String url;
	private String instance;

	private Algorithms(final String url, final String instance)
	{
		this.url = url;
		this.instance = instance;
	}
	public String getURL ()
	{
		return url;
	}
	public String getValue ()
	{
		return instance;
	}
}
