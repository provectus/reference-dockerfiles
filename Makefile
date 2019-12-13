all:
	# Remove comments and blank lines
	cat java/Dockerfile | sed -e 's/\s*#.*$$//' | sed -e '/^\s*$$/d' > Dockerfile-prod-java
	cat python/Dockerfile | sed -e 's/\s*#.*$$//' | sed -e '/^\s*$$/d' > Dockerfile-prod-python
clean:
	rm -f Dockerfile-prod-*
