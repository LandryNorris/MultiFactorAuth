MultiFactor
===========

This server provides for Multi-Factor authentication of devices and accounts.

The process:

1. Register an account with Multi-Factor. This will create a public key associated with your account.
2. Register a device with your account. This device will act as the verifier.
3. When you try to sign in using Multi-Factor, a notification will be sent to the registered device. 
Once you verify the request on the verifying device, the login will succeed.

The technical process:
1. A request is made to MultiFactor, giving the account ID.
2. The server generates a token for the verification process. 
This token will be used throughout the process.
3. The server looks up how to contact the verifying devices.
4. When the user interacts with a verifier device, the verifier will 
notify the server.
5. Every verifying device keeps a private key that corresponds with a 
public key held by the server. This allows the server to verify the device's 
identity. The server sends a challenge to the verifier device to confirm 
its identity.
6. Once the identity of the verifier is confirmed, the server will inform 
the original device that it is safe to authenticate.

Note on CVE-2021-44228 and CVE-2021-45046
=========================================

This project uses Logback for logging, so it is not affected by the Log4J 
CVEs.