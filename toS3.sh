#!/bin/bash

# send ./Materials/* to AWS S3

aws s3 cp --recursive ./Materials/ s3://using-materials-demo/Materials/
aws s3 cp --recursive ./Reports/   s3://using-materials-demo/Reports/

echo see https://s3-ap-northeast-1.amazonaws.com/using-materials-demo/Materials/index.html#
