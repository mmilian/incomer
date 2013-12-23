<!DOCTYPE html>
<html>
   <head>
      <meta name="layout" content="main"/>
      <r:require modules="bootstrap"/>
   </head>
	<body>
		<ul class="nav nav-tabs">
		  <li class="active"><a href="#dotPay" data-toggle="tab">dotPay</a></li>
  		  <li><a href="#paypal" data-toggle="tab">paypal</a>			
  		  </li>
  		  <li><a href="#iPko" data-toggle="tab">iPko</a></li>
		</ul>
		<div class="tab-content">
  			<div class="tab-pane active" id="dotPay">
  				<div class="row">
					<g:uploadForm controller="payment" action="uploadDotPay">
        			<input type="file" name="dotPayCsv" />
        			<input type="submit"/>
    			</g:uploadForm>
				</div>
  			</div>
  			<div class="tab-pane" id="paypal">
  				<div class="row">
					<g:uploadForm controller="payment" action="uploadPayPal">
        			<input type="file" name="uploadPayPal" />
        			<input type="submit"/>
    			</g:uploadForm>
				</div>
  			</div>
  			<div class="tab-pane" id="iPko">
  				<div class="row">
					<g:uploadForm controller="payment" action="uploadIpko">
        			<input type="file" name="uploadIpko" />
        			<input type="submit"/>
    				</g:uploadForm>
				</div>
  			</div>
		</div>
		
	</body>
</html>