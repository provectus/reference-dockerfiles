#!/usr/env/python3
import boto3, sys, signal, socket
from http.server import BaseHTTPRequestHandler, HTTPServer
from time import sleep

def termination_handler(sig, frame):
  print('Shutting down gracefully...')
  sys.exit(0)

def run(server_class=HTTPServer, handler_class=BaseHTTPRequestHandler):
    server_address = ('', 8080)
    httpd = server_class(server_address, handler_class)
    httpd.serve_forever()

if __name__ == "__main__":
  print("Initializing...")
  signal.signal(signal.SIGINT, termination_handler)
  s3 = boto3.resource('s3')
  print("Serving web content on 0.0.0.0:8080")
  run()