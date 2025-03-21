//package com.blog.page.core.models.impl;
//
//import com.blog.page.core.models.HeaderModel;
//import org.apache.sling.api.resource.Resource;
//import org.apache.sling.api.resource.ValueMap;
//import org.apache.sling.models.annotations.DefaultInjectionStrategy;
//import org.apache.sling.models.annotations.Model;
//import org.apache.sling.models.annotations.injectorspecific.ChildResource;
//import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
//
//import javax.inject.Inject;
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.stream.StreamSupport;
//
//@Model(
//        adaptables = Resource.class,
//        adapters = HeaderModel.class,
//        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
//)
//public class HeaderModelImpl implements HeaderModel {
//
//    @Inject
//    Resource resource;
//
//    @ValueMapValue
//    private String title;
//
//    @ValueMapValue
//    private String logo;
//
//    @ValueMapValue
//    private String[] text;
//
//    @ValueMapValue
//    private String[] links;
//
//
//    @Override
//    public String getTitle() {
//        return title;
//    }
//
//    @Override
//    public String getLogo() {
//        return logo;
//    }
//
////    @ChildResource
////    private Resource actions;
//
//
//
////    @Override
////    public List<Map<String, String>> getNavItems() {
////        List <Map<String,String>> navbarItems=new ArrayList<>();
////        Resource navDetails=resource.getChild("actions");
////        if(navDetails!=null){
////            for(Resource nav:navDetails.getChildren()){
////                Map<String,String> navMap=new HashMap<>();
////                navMap.put("text", nav.getValueMap().get("text", String.class));
////                navMap.put("link", nav.getValueMap().get("link", String.class));
////                navbarItems.add(navMap);
////            }
////        }
////        return navbarItems;
////    }
//
//    @Override
//    public List<Map<String, String>> getNavItems() {
//        List<Map<String, String>> navbarItems = new ArrayList<>();
//
//        Resource navDetails = resource.getChild("actions"); // Keep original structure
//        if (navDetails == null) {
//            return Collections.emptyList(); // Avoid NullPointerException
//        }
//
//        for (Resource nav : navDetails.getChildren()) {
//            ValueMap navProperties = nav.getValueMap();
//            String textValue = navProperties.get("text", String.class);
//            String linkValue = navProperties.get("link", String.class);
//
//            if (textValue != null && linkValue != null) { // Avoid adding null values
//                Map<String, String> navMap = new HashMap<>();
//                navMap.put("text", textValue);
//                navMap.put("link", linkValue);
//                navbarItems.add(navMap);
//            }
//        }
//        return navbarItems;
//    }
//
//
//
//
//
//
//}
package com.blog.page.core.models.impl;

import com.blog.page.core.models.HeaderModel;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;
import java.util.*;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = HeaderModel.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class HeaderModelImpl implements HeaderModel {

    @SlingObject
    ResourceResolver resolver;   // to manipulate the navlink page to find the hideInNav property

    @SlingObject
    private Resource componentResource;

//    @Inject
//    private Resource resource;

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String logo;
//websitename=title
    //
//    @ChildResource(name = "actions")  // Fix: Ensure correct path
//    private Resource actions;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getLogo() {
        return logo;
    }

    @Override
    public List<Map<String, String>> getNavItems() {
        List<Map<String, String>> navbarItems = new ArrayList<>();
        Resource navItems = componentResource.getChild("actions");

        if (navItems != null) {
            for (Resource action : navItems.getChildren()) {
                ValueMap navProperties = action.getValueMap();
                String textValue = navProperties.get("text", String.class);
                String linkValue = navProperties.get("link", String.class);

                // Fetch the linked page resource
                if ( linkValue != null) {
                    PageManager pageManager = resolver.adaptTo(PageManager.class);
                    Page page = pageManager.getPage(linkValue);
                    String hideNavProperty = page.getProperties().get("hideInNav","Default");

                    Map<String,String> navItem = new HashMap<>();
                    navItem.put("text",textValue);
                    navItem.put("link",linkValue+".html");
                    navItem.put("hideProp",hideNavProperty);

                    navbarItems.add(navItem);
                }
            }
        }
        return navbarItems;
    }

}

