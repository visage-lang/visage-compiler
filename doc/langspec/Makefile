LANGSPEC_HTML_DIR = ../../build/doc/html

langspec:
	xsltproc -xinclude  --stringparam use.id.as.filename 1 --stringparam chunk.section.depth 0 --stringparam chunk.fast 1 -o $(LANGSPEC_HTML_DIR)/ ../../tools/docbook-xsl/xhtml/chunk.xsl JavaFX-Language.xml

StringLiteral.html: StringLiteral.xml
	xsltproc ../../tools/docbook-xsl/html/docbook.xsl StringLiteral.xml >StringLiteral.html
