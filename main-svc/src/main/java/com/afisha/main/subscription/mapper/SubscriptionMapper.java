package com.afisha.main.subscription.mapper;

import com.afisha.main.enums.FriendshipsStatus;
import com.afisha.main.subscription.dto.NewRequestSubscription;
import com.afisha.main.subscription.dto.SubscriberData;
import com.afisha.main.subscription.dto.SubscriptionDto;
import com.afisha.main.subscription.model.Subscription;
import com.afisha.main.user.mapper.UserMapper;
import com.afisha.main.user.model.User;

import java.time.LocalDateTime;

public class SubscriptionMapper {

    public static Subscription toNewSubscriptionFromRequest(User subscriber, NewRequestSubscription requestSubscription, User owner) {
        return Subscription.builder()
                .follower(subscriber)
                .owner(owner)
                .subscribeTime(LocalDateTime.now())
                .unsubscribeTime(null)
                .friendshipsStatus(FriendshipsStatus.NO_FRIENDSHIP)
                .build();
    }

    public static SubscriptionDto toSubscriptionDto(Subscription subscription) {
        return SubscriptionDto.builder()
                .id(subscription.getId())
                .followerId(subscription.getFollower().getId())
                .owner(UserMapper.toUserDto(subscription.getOwner()))
                .subscribeTime(subscription.getSubscribeTime())
                .unsubscribeTime(subscription.getUnsubscribeTime())
                .friendshipsStatus(subscription.getFriendshipsStatus())
                .build();
    }

    public static SubscriptionDto toSubscriptionDtoWithoutUnsubscribeTime(Subscription subscription) {
        return SubscriptionDto.builder()
                .id(subscription.getId())
                .followerId(subscription.getFollower().getId())
                .owner(UserMapper.toUserDto(subscription.getOwner()))
                .subscribeTime(subscription.getSubscribeTime())
                .friendshipsStatus(subscription.getFriendshipsStatus())
                .build();
    }

    public static SubscriberData toSubscriberData(Subscription subscription) {
        return SubscriberData.builder()
                .userId(subscription.getFollower().getId())
                .ownerName(subscription.getOwner().getName())
                .subscribeTime(subscription.getSubscribeTime())
                .friendshipsStatus(subscription.getFriendshipsStatus())
                .build();
    }

}