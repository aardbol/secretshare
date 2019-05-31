# Project
API for sharing time limited encrypted secrets over the internet.

This project was created to allow sharing sensitive information by email in a more secure way, but is not limited to such
use cases.

Many have the bad habit of sharing sensitive information such as passwords by email in plaintext format, 
which causes several security risks. One is that it's not sure whether the email communication is encrypted from sender to
receiver, which means it could be extracted by a third party during transit. These emails are often also stored for 
longer periods of time, either in the user's inbox or other folder, or just in their recycle bin, while the password is left
unchanged during this time. Anyone one else who gets access to the user's mailbox (email provider, replacement colleague, other user of 
a shared mailbox, boss, hacker,...) also gets access to these passwords. This project is created to make sharing of such 
secret information limited in time while at the same time encouraging the use of the right tools for secure storage.

You can either use the hosted version at https://secretshare.wheredoi.click, or you can host it yourself for even more
privacy.

# Features
* Time limit for shares between 5 minutes and 30 days
* A web frontend is available at https://github.com/aardbol/secretshareweb
* Keep records of when a secret was accessed until the secret is expired

# Requirements for running (or tested with)
* Java 11
* Tomcat 9
* PostgreSQL 11
* A-grade TLS encryption

# License
Open source under the EUROPEAN UNION PUBLIC LICENCE v. 1.2 

See LICENSE (English), LICENTIE (Dutch) or https://joinup.ec.europa.eu/collection/eupl/eupl-text-11-12 for other languages.
Check the bottom of the license for compatible licenses such as the GPL v2+
