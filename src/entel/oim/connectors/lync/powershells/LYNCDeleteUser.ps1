Try{
   
   # Getting parameters
   $identity=$args[0];
   
   # Checking filter
   If (($identity -ne "NULL") -and (-not ([string]::IsNullOrEmpty($identity)))) {

      # Invoque command
      $comand="Get-CsAdUser -LdapFilter ""sAMAccountName=$identity"" | Disable-CsUser"
      Write-Host "Comand to execute:"
      Write-Host $comand
      Write-Host "Initiating invocation..."
      Invoke-Expression $comand

      # Success Flag
      If ($?) {
	     Write-Host "POWERSHELL COMPLETED SUCCESSFULLY"
      }

   } Else {
      # Error with user
      Write-Host "Invocation error. Identity must be provider"
   }

} Catch {

   # Exception
   Write-Host $_
}
