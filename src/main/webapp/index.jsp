<!DOCTYPE html>

<!-- This file is part of aida. It is subject to the license terms in
     the LICENSE file found in the top-level directory of this distribution.
     You may not use this file except in compliance with the License. -->

<html lang="en">
<head>
    <title>aida</title>
    <meta charset="utf-8">
    
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://codemirror.net/lib/codemirror.css">
    
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <script src="https://codemirror.net/lib/codemirror.js"></script>
    <script src="http://codemirror.net/mode/turtle/turtle.js"></script>
    <script src="http://codemirror.net/mode/sparql/sparql.js"></script>
    <script src="http://codemirror.net/mode/xml/xml.js"></script>
    <script src="https://codemirror.net/addon/display/autorefresh.js"></script>
    <script src="http://www.appelsiini.net/projects/chained/jquery.chained.min.js"></script>
    
<style type="text/css">
    html, body {
        height: 100%;
        margin: 0px;
    }
    .nav {
        /* background: #e3d235; */
    }
    .panel-body {
        height: 100%;
        /* background: #f0e68c; */
    }
    .CodeMirror {
        border: 1px solid #eee;
        height: auto;
    }
    .outerPanel {
        padding-top: 10px;
        padding-right: 10px;
        padding-bottom: 10px;
        padding-left: 10px;
        background: #f2f2f2
    }
</style>    
</head>
    
<body>
        
<div class="container">
    <br/>
    <div class="panel panel-default outerPanel">
    <div class="panel panel-default">
        <div class="panel-body">
            <p><b>fReSCO</b> is yet another REST API for multi-format RDF conversion.</p>
        </div>
    </div>
    
    <ul class="nav nav-tabs">
        <li class="active"><a data-toggle="tab" href="#home">Input by URI</a></li>
        <li><a data-toggle="tab" href="#menu1">Input by text</a></li>
        <li><a data-toggle="tab" href="#menu2">Output</a></li>
        <li><a data-toggle="tab" href="#menu3">About</a></li>
    </ul>

    <div class="panel panel-default">
        <div class="panel-body">
            <div class="tab-content">
                <div id="home" class="tab-pane fade in active">
                    <br/>
                    <form class="form-horizontal" id="convertByGET">

                        <fieldset>
                            <div class="form-group row">
                                <label class="col-md-2 control-label" for="inputUri">RDF documentURI</label>
                                <div class="col-md-10">
                                    <input id="inputUri" name="inputUri" type="text" placeholder="http://xmlns.com/foaf/spec/index.rdf" class="form-control input-md" required="">
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-md-2 control-label" for="inputFormatByURI">Import Format</label>
                                <div class="col-md-10">
                                    <select id="inputFormatByURI" name="inputFormatByURI" class="form-control" required="">
                                        <option value="">--</option>
                                        <option value="json-ld">JSON-LD</option>
                                        <option value="n3">N3</option>
                                        <option value="n-quads">N-Quads</option>
                                        <option value="n-triples">N-Triples</option>
                                        <option value="rdf-json">RDF-JSON</option>
                                        <option value="rdf-xml">RDF/XML</option>
                                        <option value="trig">TRIG</option>
                                        <option value="trix">TRIX</option>
                                        <option value="turtle">Turtle</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-md-2 control-label" for="exportFormatByURI">Export Format</label>
                                <div class="col-md-10">
                                    <select id="exportFormatByURI" name="exportFormatByURI" class="form-control" required="">
                                        <option value="">--</option>
                                        <option value="json-ld">JSON-LD</option>
                                        <option value="n3">N3</option>
                                        <option value="n-quads">N-Quads</option>
                                        <option value="n-triples">N-Triples</option>
                                        <option value="rdf-json">RDF-JSON</option>
                                        <option value="rdf-xml">RDF/XML</option>
                                        <option value="trig">TRIG</option>
                                        <option value="trix">TRIX</option>
                                        <option value="turtle">Turtle</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label" for="submit"></label>
                                <div class="col-md-10">
                                    <button id="submit" name="submit" class="btn btn-info">Convert RDF</button>
                                </div>
                            </div>
                        </fieldset>
                    </form>
                </div>
            
                <div id="menu1" class="tab-pane fade">
                    <form class="form-horizontal" id="convertByPOST">
                        <fieldset>
                        	<!-- 
                            <div class="form-group row">
                                <label class="col-md-2 control-label" for="baseURI">Export baseURI</label>  
                                <div class="col-md-10">
                                    <input id="baseURI" name="baseURI" type="text" placeholder="http://xmlns.com/foaf/0.1/" class="form-control input-md" required="">   
                                </div>
                            </div>
                            -->
                            <div class="form-group row">
                                <label class="col-md-2 control-label" for="textarea">Text Area</label>
                                <div class="col-md-10">                     
                                    <textarea class="form-control" id="textarea" rows="10" name="textarea" required="" placeholder="Put your RDF here!"></textarea>
                                </div>
                            </div>
                           <div class="form-group row">
                                <label class="col-md-2 control-label" for="inputFormatByText">Import Format</label>
                                <div class="col-md-10">
                                    <select id="inputFormatByText" name="inputFormatByText" class="form-control" required="">
                                        <option value="">--</option>
                                        <option value="json-ld">JSON-LD</option>
                                        <option value="n3">N3</option>
                                        <option value="n-quads">N-Quads</option>
                                        <option value="n-triples">N-Triples</option>
                                        <option value="rdf-json">RDF-JSON</option>
                                        <option value="rdf-xml">RDF/XML</option>
                                        <option value="trig">TRIG</option>
                                        <option value="trix">TRIX</option>
                                        <option value="turtle">Turtle</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-md-2 control-label" for="exportFormatByText">Export Format</label>
                                <div class="col-md-10">
                                    <select id="exportFormatByText" name="exportFormatByText" class="form-control" required="">
                                        <option value="">--</option>
                                        <option value="json-ld">JSON-LD</option>
                                        <option value="n3">N3</option>
                                        <option value="n-quads">N-Quads</option>
                                        <option value="n-triples">N-Triples</option>
                                        <option value="rdf-json">RDF-JSON</option>
                                        <option value="rdf-xml">RDF/XML</option>
                                        <option value="trig">TRIG</option>
                                        <option value="trix">TRIX</option>
                                        <option value="turtle">Turtle</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label" for="submit"></label>
                                <div class="col-md-10">
                                    <button id="submit" name="submit" class="btn btn-info">Convert RDF</button>
                                </div>
                            </div>
                        </fieldset>
                    </form>
                </div>

                <div id="menu2" class="tab-pane fade">
                    <form>
                        <textarea id="editor" name="editor"></textarea>
                    </form>
                </div>
                    
                <div id="menu3" class="tab-pane fade">
                    <h3>What does fReSCO do?</h3>
                    <p>fReSCO is yet another REST API for multi-format RDF conversion.</p>
                    <p>It provides syntax translations between data formats ranging from RDF/XML to Turtle or Trix.</p>
                    <p>Conversions triggered either by URI or by direct text input.</p>
                    <h3>Get your own fReSCO!</h3>
                    <p>The source code of this tool is available from <a href="https://github.com/rmrschub/fresco">GitHub</a>.
                </div>
                </div>
            </div>
        </div>

        <div class="panel panel-default">
            <div class="panel-body">
                <p>This work has been supported by the <a href="http://www.bmbf.de/en/index.html">German Ministry for Education and Research (BMBF)</a> as part of the <a href="http://www.arvida.de/">ARVIDA project.</a></p>
            </div>
        </div>
    </div>    
</div>


<script type="text/javascript">
    $("#exportFormatByURI").chainedTo($("#importFormatByURI"));
    $("#exportFormatByText").chainedTo($("#importFormatByText"));
</script>
    
<script type="text/javascript">
    var editor = CodeMirror.fromTextArea(document.getElementById("editor"), {
        viewportMargin: Infinity,
        readOnly: true,
        styleActiveLine: true,
        mode: "text/turtle",
        autoRefresh: true
    });
    $('#editor').data('CodeMirrorInstance', editor);
</script>
   
<script type="text/javascript">
    $("#convertByGET").submit(function(event) {
        event.preventDefault();
        
        var importFormat = $("#inputFormatByURI").val();
    	var exportFormat = $("#exportFormatByURI").val();
    	var inputURI = $("#inputUri").val();
    	
        $.get( 
        		"api/"+importFormat+"/"+exportFormat+"?uri="+inputURI,
        		function(data) {
                	$('.nav-tabs a[href="#menu2"]').tab('show');
                	var editor = $('#editor').data('CodeMirrorInstance');
                	editor.setOption("mode", "text/turtle");
                	if (exportFormat=="json-ld" || exportFormat=="rdf-json")
                		editor.getDoc().setValue(JSON.stringify(data));
                   	else
                		editor.getDoc().setValue(data);
                	editor.refresh();
            	}
        );
    });
</script>

<script type="text/javascript">
    $("#convertByPOST").submit(function(event) {
        event.preventDefault();
        
        var importFormat = $("#inputFormatByText").val();
    	var exportFormat = $("#exportFormatByText").val();
    	
        $.post( 
        		"api/"+importFormat+"/"+exportFormat,
        		$("#textarea").val(),
            	function(data) {
                	$('.nav-tabs a[href="#menu2"]').tab('show');
					var editor = $('#editor').data('CodeMirrorInstance');
                	editor.setOption("mode", "text/turtle");
                	if (exportFormat=="json-ld" || exportFormat=="rdf-json")
                		editor.getDoc().setValue(JSON.stringify(data));
                   	else
                		editor.getDoc().setValue(data);
                	editor.refresh();
            	}
        );
        
    });
</script>

<style type='text/css'>@import url('http://getbarometer.s3.amazonaws.com/assets/barometer/css/barometer.css');</style>
<script src='http://getbarometer.s3.amazonaws.com/assets/barometer/javascripts/barometer.js' type='text/javascript'></script>
<script type="text/javascript" charset="utf-8">
  BAROMETER.load('D2D88y7fGGhLd72ahB937');
</script>        

</body>
</html>