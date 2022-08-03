#!/usr/bin/env bash

REPOSITORY=/home/ec2-user/

if [ -d $REPOSITORY/ ]; then
    rm -rf $REPOSITORY/service
fi
mkdir -vp $REPOSITORY/service