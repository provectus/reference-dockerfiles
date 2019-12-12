all:
	# Remove comments and blank lines
	cat Dockerfile | sed -e 's/\s*#.*$$//' | sed -e '/^\s*$$/d' > Dockerfile-prod
clean:
	rm -f Dockerfile-prod