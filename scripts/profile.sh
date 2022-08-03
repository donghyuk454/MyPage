#!/bin/bash

function find_profile()
{
    CURRENT_PROFILE=$(curl -s http://localhost/profile)

    if [ $CURRENT_PROFILE == set1 ]
    then
        IDLE_PROFILE=set2
    elif [ $CURRENT_PROFILE == set2 ]
    then
        IDLE_PROFILE=set1
    else
        IDLE_PROFILE=set1
    fi

    echo "${IDLE_PROFILE}"
}

function find_port()
{
    IDLE_PROFILE=$(find_profile)

    if [ $IDLE_PROFILE == set1 ]
    then
        IDLE_PORT=8081
    elif [ $IDLE_PROFILE == set2 ]
    then
        IDLE_PORT=8082
    fi

    echo "${IDLE_PORT}"
}

function find_switch_port()
{
    CONTAINER_ID=$(sudo docker ps -f "ancestor=set2" -q)

    if [ -z $CONTAINER_ID ]
    then
        echo "8081"
    else
        find_port
    fi
}