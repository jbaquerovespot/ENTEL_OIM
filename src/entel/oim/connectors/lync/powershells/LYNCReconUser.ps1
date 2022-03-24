Try{
   
   # Getting parameters
   $identity=$args[0];

   # Checking filter
   If (($identity -ne "NULL") -and (-not ([string]::IsNullOrEmpty($identity)))) {
   
      # Invoque command
      $comand="Get-CsUser -LdapFilter ""sAMAccountName=$identity"" | Format-List -Property Identity, SamAccountName, AudioVideoDisabled, Enabled, RegistrarPool, EnterpriseVoiceEnabled, HostedVoiceMail, LineURI, LineServerURI, PrivateLine, RemoteCallControlTelephonyEnabled, SipAddress"
      Write-Host "Comand to execute:"
      Write-Host $comand
      Write-Host "Initiating invocation..."
      Invoke-Expression $comand

      # Success Flag
      If ($?) {
	     Write-Host "POWERSHELL COMPLETED SUCCESSFULLY"
      }
   
   } Else {
      
	  # Invoque command
      $comand="Get-CsUser | Format-List -Property Identity, SamAccountName, AudioVideoDisabled, Enabled, RegistrarPool, EnterpriseVoiceEnabled, HostedVoiceMail, LineURI, LineServerURI, PrivateLine, RemoteCallControlTelephonyEnabled, SipAddress"
      Write-Host "Comand to execute:"
      Write-Host $comand
      Write-Host "Initiating invocation..."
      Invoke-Expression $comand

      # Success Flag
      If ($?) {
	     Write-Host "POWERSHELL COMPLETED SUCCESSFULLY"
      }
   }

} Catch {
   # Exception
   Write-Host $_
}