package com.afisha.main.subscription.service;

import com.afisha.main.event.dto.EventShortDto;
import com.afisha.main.subscription.dto.NewRequestSubscription;
import com.afisha.main.subscription.dto.SubscriberData;
import com.afisha.main.subscription.dto.SubscriptionDto;

import java.util.List;

public interface SubscriptionService {
    SubscriptionDto subscribe(Long userId, NewRequestSubscription requestSubscription);

    void unsubscribe(Long userId, Long ownerId);

    List<EventShortDto> getEventsFromSubscriptions(Long userId, int from, int size);

    Long getSubscriberCount(Long userId);

    List<SubscriberData> getAllSubscribers(Long userId, int from, int size);
}