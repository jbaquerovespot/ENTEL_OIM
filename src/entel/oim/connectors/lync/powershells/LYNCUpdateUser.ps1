Try{
   
   # Getting parameters
   $identity=$args[0];
   $audioVideoDisabled=$args[1];
   $enabled=$args[2];
   $enterpriseVoiceEnabled=$args[3];
   $hostedVoiceMail=$args[4];
   $lineURI=$args[5];
   $lineServerURI=$args[6];
   $privateLine=$args[7];
   $remoteCallControlTelephonyEnabled=$args[8];
   $sipAddress=$args[9];

   # Variable
   $isValid=$false;

   # Checking filter
   If (($identity -ne "NULL") -and (-not ([string]::IsNullOrEmpty($identity)))) {
   
      # Create the command to execute
      $comand="Get-CsUser -LdapFilter ""sAMAccountName=$identity"" | Set-CsUser";
      If (($audioVideoDisabled -ne "NULL") -and (-not ([string]::IsNullOrEmpty($audioVideoDisabled)))) {
         $comand="$comand -AudioVideoDisabled `$$audioVideoDisabled";
         $isValid=$true;
      }
      If (($enabled -ne "NULL") -and (-not ([string]::IsNullOrEmpty($enabled)))) {
         $comand="$comand -Enabled `$$enabled";
         $isValid=$true;
      }
      If (($enterpriseVoiceEnabled -ne "NULL") -and (-not ([string]::IsNullOrEmpty($enterpriseVoiceEnabled)))) {
         $comand="$comand -EnterpriseVoiceEnabled `$$enterpriseVoiceEnabled";
         $isValid=$true;
      }
      If (($hostedVoiceMail -ne "NULL") -and (-not ([string]::IsNullOrEmpty($hostedVoiceMail)))) {
         $comand="$comand -HostedVoiceMail `$$hostedVoiceMail";
         $isValid=$true;
      }
      If (($lineURI -ne "NULL") -and (-not ([string]::IsNullOrEmpty($lineURI)))) {
         $comand="$comand -LineURI $lineURI";
         $isValid=$true;
      }
      If (($lineServerURI -ne "NULL") -and (-not ([string]::IsNullOrEmpty($lineServerURI)))) {
         $comand="$comand -LineServerURI $lineServerURI";
         $isValid=$true;
      }
      If (($privateLine -ne "NULL") -and (-not ([string]::IsNullOrEmpty($privateLine)))) {
         $comand="$comand -PrivateLine $privateLine";
         $isValid=$true;
      }
      If (($remoteCallControlTelephonyEnabled -ne "NULL") -and (-not ([string]::IsNullOrEmpty($remoteCallControlTelephonyEnabled)))) {
         $comand="$comand -RemoteCallControlTelephonyEnabled `$$remoteCallControlTelephonyEnabled";
         $isValid=$true;
      }
      If (($sipAddress -ne "NULL") -and (-not ([string]::IsNullOrEmpty($sipAddress)))) {
         $comand="$comand -SipAddress $sipAddress";
         $isValid=$true;
      }

      # CHecking if valid invocation
      If ($isValid) {

         # Invoke command
         Write-Host "Comand to execute:"
         Write-Host $comand
         Write-Host "Initiating invocation..."
         Invoke-Expression $comand
   
         # Success Flag
         If ($?) {
	        Write-Host "POWERSHELL COMPLETED SUCCESSFULLY"
         }
      } Else {
         # Error with parameters
         Write-Host "Invocation error. All properties must be provided"
      }

   } Else {
      # Error with user
      Write-Host "Invocation error. Identity must be provider"
   }
   
} Catch {
   # Exception
   Write-Host $_
}
