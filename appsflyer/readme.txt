For correct work with google storage you must create environment variable GOOGLE_APPLICATION_CREDENTIALS.
It must link to json with fields "type", "project_id", "private_key_id", "private_key", "client_email", "client_id", "auth_uri", "token_uri", "auth_provider_x509_cert_url","client_x509_cert_url"
For example add export entry in Tomcat startup.sh