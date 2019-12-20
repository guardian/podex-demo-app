# PodX

A project to extend the podcast specification to make it easier to make enhanced podcasts

https://www.theguardian.com/info/2019/jun/12/why-we-want-to-make-podcasts-better

A better podcast app, with a goal to improve listener revenue.

A potential mockup of the UI and some of the supported events:
![](https://i.guim.co.uk/img/media/05c99affe78e9acdca4176b848e0d7fb6d75cd32/0_0_1642_682/master/1642.jpg?width=1920&quality=85&auto=format&fit=max&s=16a112a169bd81614113700e7f5a1f6e)

# PodX Feed Specification

## What is PodX?

PodX is an extension for a podcast’s RSS 2.0 feed.

PodX is an XML namespace designed to be consumed by a podcast client application. PodX event data consists of elements which specify information or interactions to display to a listener of a podcast. Valid elements are identified by name, and contain a set common child elements which specify a time to display information, as well as text descriptions of displayed information to be shown through the client application.

All PodX event elements are expected to be children of item elements, where the item encapsulates an episode of a podcast in a feed.


## PodX Event Elements


<table>
  <tr>
   <td><strong>Element</strong>
   </td>
   <td><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td>web
   </td>
   <td>A web link event contains a href attribute that points to a URL a user can choose to navigate to during the display of en event.
<p>

Example:

```
<podx:web url=”https://www.example.com”>
  <podx:start>1:01</podx:start>
  <podx:end>1:31</podx:end>
  <podx:caption>Check out this relevant web link</podx:caption>
  <podx:notification>Example link</podx:notification>
</podx:web>
```
   </td>
  </tr>
  <tr>
   <td>image
   </td>
   <td>An event contains a href attribute that points to the URL of an image that the user can navigate to, or the PodX client can display

<p>
Example:

```
<podx:image href=”https://www.example.com/example.png ''>
  <podx:start>1:01</podx:start>
  <podx:end>1:31</podx:end>
  <podx:caption>Check out this cool image</podx:caption>
  <podx:notification>Example image</podx:notification>
</podx:image>
```
   </td>
  </tr>
  <tr>
   <td>socialPrompt
   </td>
   <td>An event that contains a list of urls and social media services so that users can choose from a range of options for engaging with podcast producers.
<p>
Example:

```
<podx:socialPrompt>
  <podx:start>1:01</podx:start>
  <podx:end>1:31</podx:end>
  <podx:caption>Follow us on these</podx:caption>
  <podx:notification>Follow us</podx:notification>
  <podx:socialLink url=”https://www.facebook.com/theguardianaustralia”/>
  <podx:socialLink url=”https://www.instagram.com/guardianaustralia”/>
</podx:socialPrompt>
```
   </td>
  </tr>
  <tr>
   <td>text
   </td>
   <td>An event to show text from the caption element without additional unique elements or attributes.
<p>
Example:

```
<podx:text>
  <podx:start>1:01</podx:start>
  <podx:end>1:31</podx:end>
  <podx:caption>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</podx:caption>
  <podx:notification>Lorem ipsum</podx:notification>
</podx:text>
```
   </td>
  </tr>
  <tr>
   <td>support
   </td>
   <td>A web link event pointing to a site that allows a listener to support podcast producers. Identical to the web link event in structure and function, but enables the podcast client application to differentiate for display.
<p>
Example:

```
<podx:support url=”https://support.theguardian.com/au/support”>
  <podx:start>1:01</podx:start>
  <podx:end>1:31</podx:end>
  <podx:caption>Support our efforts to podcast</podx:caption>
  <podx:notification>Support us</podx:notification>
</podx:support>
```
   </td>
  </tr>
  <tr>
   <td>feedLink
   </td>
   <td>A web link event with an additional attribute to point to an item element or episode of a podcast feed pointed to by the URL. A third optional attribute specifies a point in normal play time from the start of playback to begin playing. This way the user can be taken to a specific point in a different podcast episode and begin playback at that point.
<p>
Example:

```
<podx:feedLink>
  <podx:start>1:01</podx:start>
  <podx:end>1:31</podx:end>
  <podx:caption>Support our efforts to podcast</podx:caption>
  <podx:notification>Support us</podx:notification>
  <podx:feedUrl>https://interactive.guim.co.uk/podx/podcast.xml</podx:feedUrl>
  <podx:feedItemTitle> A whole lot of images </podx:feedItemTitle>
  <podx:feedItemPubDate>Fri, 08 Nov 2019 19:00:43 GMT</podx:feedItemPubDate>
  <podx:feedItemGuid>5dc4f4838f08c2d1f80dbe45</podx:feedItemGuid>
  <podx:feedItemEnclosureUrl>https://flex.acast.com/audio.guim.co.uk/2019/11/08-18765-APLlaborreport.mp3</podx:feedItemEnclosureUrl>
  <podx:feedItemAudioTimeStamp>1:11</podx:feedItemAudioTimeStamp>
</podx:FeedLink>
```
   </td>
  </tr>
  <tr>
   <td>callPrompt
   </td>
   <td>Contact details to initiate a call or text to a predefined contact number. The number to contact is specified as an attribute.
<p>
Example:

```
<podx:callPrompt number=”01189998819991197253”>
  <podx:start>1:01</podx:start>
  <podx:end>1:31</podx:end>
  <podx:caption>Call out chat line</podx:caption>
  <podx:notification>Call us</podx:notification>
</podx:callPrompt>
```
   </td>
  </tr>
  <tr>
   <td>newsletterSignup
   </td>
   <td>A web link event pointing to a form to enter details required to sign up to a newsletter. Identical to the web link event in structure and function, but enables the podcast client application to differentiate for display.
<p>
Example:

```
<podx:newsletterSignup url=”https://www.theguardian.com/world/guardian-australia-morning-mail/2014/jun/24/-sp-guardian-australias-morning-mail-subscribe-by-email”>
  <podx:start>1:01</podx:start>
  <podx:end>1:31</podx:end>
  <podx:caption>Sign up to the morning mail</podx:caption>
  <podx:notification>Morning mail</podx:notification>
</podx:newsletterSignup>
```
   </td>
  </tr>
  <tr>
   <td>feedback
   </td>
   <td>A web link event pointing to a form to submit feedback to podcast producers. Identical to the web link event in structure and function, but enables the podcast client application to differentiate for display.
<p>
Example:

```
<podx:feedback url=”https://www.theguardian.com/info/2013/may/26/contact-guardian-australia”>
  <podx:start>1:01</podx:start>
  <podx:end>1:31</podx:end>
  <podx:caption>Contact us to let us know what you think</podx:caption>
  <podx:notification>Contact us</podx:notification>
</podx:feedback>
```
   </td>
  </tr>
  <tr>
   <td>poll
   </td>
   <td>A web link event pointing to a poll or survey. Identical to the web link event in structure and function, but enables the podcast client application to differentiate for display.
<p>
Example:

```
<podx:poll url=”https://www.theguardian.com/environment/ng-interactive/2019/oct/27/australian-bird-of-the-year-2019-vote-for-your-favourite”>
  <podx:start>1:01</podx:start>
  <podx:end>1:31</podx:end>
  <podx:caption>Vote in this poll</podx:caption>
  <podx:notification>Vote here</podx:notification>
</podx:poll>
```
   </td>
  </tr>
</table>



## PodX Common Elements

Common elements are elements that are available or required children for PodX event elements.


<table>
  <tr>
   <td><strong>Element</strong>
   </td>
   <td><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td>start
   </td>
   <td>A point in playback of a podcast expressed in normal play time relative to the start of an audio file in the parent item. The expected format is HH:MM:SS.mmm or MM:SS.mmm or SS.mmm Leading and trailing zero values can be trimmed, and milliseconds are optional.
<p>
The parent event is to be displayed from this timestamp.
<p>
Example:

```
<podx:start>1:30</podx:start>
```
   </td>
  </tr>
  <tr>
   <td>end
   </td>
   <td>The point in playback of a podcast expressed in normal play time relative to the start of an audio file in the parent item. The expected format is the same as the <start> format.
<p>
Display of the parent event is to end at this timestamp. It is expected for the end time to a after the start time.
<p>
Example:

```
<podx:end>1:10:30.55</podx:end>
```
   </td>
  </tr>
  <tr>
   <td>caption
   </td>
   <td>Text to display to a listener along with an event.
<p>
Example:

```
<podx:caption>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</podx:caption>
```
   </td>
  </tr>
  <tr>
   <td>notification
   </td>
   <td>A condensed version of the caption, to be displayed in notifications associated with the event.
<p>
Example

```
<podx:notification>Lorem ipsum</podx:notification>
```
   </td>
  </tr>
</table>



## web attributes and elements


### Attributes


<table>
  <tr>
   <td><strong>Attribute</strong>
   </td>
   <td><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td>url
   </td>
   <td>A generic url that is to be opened in a browser
   </td>
  </tr>
</table>



## image attributes and elements


### Attributes


<table>
  <tr>
   <td><strong>Attribute</strong>
   </td>
   <td><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td>href
   </td>
   <td>A uri for an image to be displayed during playback
   </td>
  </tr>
</table>



## socialPrompt attributes and elements


### Elements


<table>
  <tr>
   <td><strong>Element</strong>
   </td>
   <td><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td>socialLink
   </td>
   <td>A link to a social media profile. The url and type are stored as attributes.
<p>
Example:

```
<podx:socialLink url=”https://www.instagram.com/guardianaustralia”/>
```
   </td>
  </tr>
</table>



## socialPrompt attributes and elements


### Attributes


<table>
  <tr>
   <td><strong>Attribute</strong>
   </td>
   <td><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td>url
   </td>
   <td>A url that points to a social network profile. The podcast client can also identify which know social media sites are present using the url.
   </td>
  </tr>
</table>



## support attributes and elements


### Attributes


<table>
  <tr>
   <td><strong>Attribute</strong>
   </td>
   <td><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td>url
   </td>
   <td>A url that points to a page that allows listeners to financially support podcast producers.
   </td>
  </tr>
</table>



## feedLink attributes and elements


### Elements


<table>
  <tr>
   <td><strong>Element</strong>
   </td>
   <td><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td>feedUrl
   </td>
   <td>The url of an rss feed that describes a podcast.
   </td>
  </tr>
  <tr>
   <td>feedItemTitle
   </td>
   <td>The title field of an item element to be used to identify the item that describes the linked episode to begin playing.
   </td>
  </tr>
  <tr>
   <td>feedItemPubDate
   </td>
   <td>The published date field of an item element to be used to identify the item that describes the linked episode to begin playing.
   </td>
  </tr>
  <tr>
   <td>feedItemGuid
   </td>
   <td>The guid field of an item element to be used to identify the item that describes the linked episode to begin playing. Guids may not be set in feeds.
   </td>
  </tr>
  <tr>
   <td>feedItemEnclosureUrl
   </td>
   <td>The the url attribute of the enclosure field of an item element to be used to identify the item that describes the linked episode to begin playing. This could be used to begin playback if other data is missing or malformed.
   </td>
  </tr>
  <tr>
   <td>feedItemAudioTimeStamp
   </td>
   <td>A timestamp to start playing an episode at in normal play time relative to the start of an audio file in the parent item. The expected format is HH:MM:SS.mmm or MM:SS.mmm or SS.mmm Leading and trailing zero values can be trimmed, and milliseconds are optional.
   </td>
  </tr>
</table>



## callPrompt attributes and elements


### Attributes


<table>
  <tr>
   <td><strong>Attribute</strong>
   </td>
   <td><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td>number
   </td>
   <td>A phone number that a listener can call
   </td>
  </tr>
</table>



## newsletterSignup attributes and elements


### Attributes


<table>
  <tr>
   <td><strong>Attribute</strong>
   </td>
   <td><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td>url
   </td>
   <td>A url that points to a newsletter signup form to be opened in a browser.
   </td>
  </tr>
</table>



## feedback attributes and elements


### Attributes


<table>
  <tr>
   <td><strong>Attribute</strong>
   </td>
   <td><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td>url
   </td>
   <td>A url that points to contact details or a feedback form to be opened in the browser.
   </td>
  </tr>
</table>



## poll attributes and elements


### Attributes


<table>
  <tr>
   <td><strong>Attribute</strong>
   </td>
   <td><strong>Description</strong>
   </td>
  </tr>
  <tr>
   <td>url
   </td>
   <td>A url that points to a poll to be opened in the browser.
   </td>
  </tr>
</table>
