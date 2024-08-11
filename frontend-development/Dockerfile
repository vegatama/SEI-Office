FROM azmifauzan/php81:apache-ext

#RUN apt update && \
#	apt upgrade -y; \
#	apt install nano -y --no-install-recommends

#ADD https://github.com/mlocati/docker-php-extension-installer/releases/latest/download/install-php-extensions /usr/local/bin/

#RUN chmod +x /usr/local/bin/install-php-extensions && \
#    install-php-extensions mcrypt mysqli pdo_mysql pgsql pdo_pgsql redis zip bcmath bz2 exif gd gettext @composer && \

RUN    a2dismod autoindex -f && a2enmod rewrite -f

COPY . /var/www/html/

WORKDIR /var/www/html

RUN composer update