Try{
   
   # Getting parameters
   $identity=$args[0];
   $registrarPool=$args[1];
   $sipDomain=$args[2];
   $sipAddressType=$args[3];

   # Checking filter
   If (($identity -ne "NULL") -and (-not ([string]::IsNullOrEmpty($identity))) -and ($registrarPool -ne "NULL") -and (-not ([string]::IsNullOrEmpty($registrarPool))) -and ($sipDomain -ne "NULL") -and (-not ([string]::IsNullOrEmpty($sipDomain))) -and ($sipAddressType -ne "NULL") -and (-not ([string]::IsNullOrEmpty($sipAddressType)))) {
   
      # Checking if user exists
      $User = Get-ADUser -LDAPFilter "(sAMAccountName=$identity)"
      If ($User -eq $Null) {

      	 # Error with user
      	 Write-Host "Invocation error. User does not exist in AD"

      } Else {

	# Invoque command
      	$comand="Get-CsAdUser -LdapFilter ""sAMAccountName=$identity"" | Enable-CsUser -RegistrarPool $registrarPool -SipDomain $sipDomain -SipAddressType $sipAddressType"
      	Write-Host "Comand to execute:"
      	Write-Host $comand
      	Write-Host "Initiating invocation..."
      	Invoke-Expression $comand

      	# Success Flag
      	If ($?) {
	   Write-Host "POWERSHELL COMPLETED SUCCESSFULLY"
      	}

      }

   } Else {
      # Error with user
      Write-Host "Invocation error. All values must be provided"
   }

} Catch {

   # Exception
   Write-Host $_
}
