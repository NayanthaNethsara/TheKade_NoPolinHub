"use client";

import { useState, useEffect } from "react";
import { useSession } from "next-auth/react";
import { motion } from "framer-motion";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { Switch } from "@/components/ui/switch";
import {
  Search,
  Plus,
  Edit,
  Eye,
  Trash2,
  MapPin,
  Clock,
  Users,
  Star,
} from "lucide-react";
import Link from "next/link";

interface Service {
  id: string;
  name: string;
  description: string;
  department: string;
  location: string;
  requirements: string[];
  estimated_duration: string;
  operating_hours: string;
  rating: number;
  total_bookings: number;
  is_active: boolean;
  created_at: string;
}

export default function ServicesPage() {
  const { data: session } = useSession();
  const [services, setServices] = useState<Service[]>([]);
  const [filteredServices, setFilteredServices] = useState<Service[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [departmentFilter, setDepartmentFilter] = useState("all");
  const [statusFilter, setStatusFilter] = useState("all");
  const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false);
  const [editingService, setEditingService] = useState<Service | null>(null);

  const [newService, setNewService] = useState({
    name: "",
    description: "",
    department: "",
    location: "",
    requirements: "",
    estimated_duration: "",
    operating_hours: "",
    is_active: true,
  });

  const departments = [
    "Transport",
    "Government",
    "Healthcare",
    "Education",
    "Banking",
    "Legal",
  ];

  useEffect(() => {
    fetchServices();
  }, []);

  useEffect(() => {
    filterServices();
  }, [services, searchTerm, departmentFilter, statusFilter]);

  const fetchServices = async () => {
    try {
      const response = await fetch("/api/services");
      if (response.ok) {
        const data = await response.json();
        setServices(data);
      }
    } catch (error) {
      console.error("Error fetching services:", error);
    } finally {
      setLoading(false);
    }
  };

  const filterServices = () => {
    let filtered = services;

    if (searchTerm) {
      filtered = filtered.filter(
        (service) =>
          service.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
          service.description.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    if (departmentFilter !== "all") {
      filtered = filtered.filter(
        (service) => service.department === departmentFilter
      );
    }

    if (statusFilter !== "all") {
      filtered = filtered.filter((service) =>
        statusFilter === "active" ? service.is_active : !service.is_active
      );
    }

    setFilteredServices(filtered);
  };

  const handleCreateService = async () => {
    try {
      const payload = {
        ...newService,
        requirements: newService.requirements
          .split(",")
          .map((req) => req.trim()),
      };

      const response = await fetch("/api/services", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      if (response.ok) {
        fetchServices();
        setIsCreateDialogOpen(false);
        setNewService({
          name: "",
          description: "",
          department: "",
          location: "",
          requirements: "",
          estimated_duration: "",
          operating_hours: "",
          is_active: true,
        });
      }
    } catch (error) {
      console.error("Error creating service:", error);
    }
  };

  const handleUpdateService = async (
    serviceId: string,
    updates: Partial<Service>
  ) => {
    try {
      const response = await fetch(`/api/services/${serviceId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updates),
      });

      if (response.ok) {
        fetchServices();
        setEditingService(null);
      }
    } catch (error) {
      console.error("Error updating service:", error);
    }
  };

  const handleDeleteService = async (serviceId: string) => {
    if (confirm("Are you sure you want to delete this service?")) {
      try {
        const response = await fetch(`/api/services/${serviceId}`, {
          method: "DELETE",
        });

        if (response.ok) {
          fetchServices();
        }
      } catch (error) {
        console.error("Error deleting service:", error);
      }
    }
  };

  const isAdmin = session?.user?.role === "admin";

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 dark:from-gray-950 dark:to-gray-900 p-6">
        <div className="max-w-7xl mx-auto">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {[...Array(6)].map((_, i) => (
              <div
                key={i}
                className="h-64 bg-white/20 dark:bg-gray-800/20 backdrop-blur-sm rounded-xl animate-pulse"
              />
            ))}
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 dark:from-gray-950 dark:to-gray-900 p-6">
      <div className="max-w-7xl mx-auto space-y-6">
        <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
          <div>
            <h1 className="text-3xl font-bold text-gray-900 dark:text-white">
              Services
            </h1>
            <p className="text-gray-600 dark:text-gray-400">
              {isAdmin
                ? "Manage all available services"
                : "Browse and book available services"}
            </p>
          </div>

          {isAdmin && (
            <Dialog
              open={isCreateDialogOpen}
              onOpenChange={setIsCreateDialogOpen}
            >
              <DialogTrigger asChild>
                <Button className="bg-gray-900 hover:bg-gray-800 text-white">
                  <Plus className="w-4 h-4 mr-2" />
                  Add Service
                </Button>
              </DialogTrigger>
              <DialogContent className="max-w-2xl bg-white/95 dark:bg-gray-900/95 backdrop-blur-sm border-gray-200/20 dark:border-gray-700/20">
                <DialogHeader>
                  <DialogTitle>Create New Service</DialogTitle>
                  <DialogDescription>
                    Add a new service to the system
                  </DialogDescription>
                </DialogHeader>
                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <Label htmlFor="name">Service Name</Label>
                    <Input
                      id="name"
                      value={newService.name}
                      onChange={(e) =>
                        setNewService({ ...newService, name: e.target.value })
                      }
                      className="bg-white/50 dark:bg-gray-800/50 border-gray-200/20 dark:border-gray-700/20"
                    />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="department">Department</Label>
                    <Select
                      value={newService.department}
                      onValueChange={(value) =>
                        setNewService({ ...newService, department: value })
                      }
                    >
                      <SelectTrigger className="bg-white/50 dark:bg-gray-800/50 border-gray-200/20 dark:border-gray-700/20">
                        <SelectValue placeholder="Select department" />
                      </SelectTrigger>
                      <SelectContent>
                        {departments.map((dept) => (
                          <SelectItem key={dept} value={dept}>
                            {dept}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>
                  <div className="col-span-2 space-y-2">
                    <Label htmlFor="description">Description</Label>
                    <Textarea
                      id="description"
                      value={newService.description}
                      onChange={(e) =>
                        setNewService({
                          ...newService,
                          description: e.target.value,
                        })
                      }
                      className="bg-white/50 dark:bg-gray-800/50 border-gray-200/20 dark:border-gray-700/20"
                    />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="location">Location</Label>
                    <Input
                      id="location"
                      value={newService.location}
                      onChange={(e) =>
                        setNewService({
                          ...newService,
                          location: e.target.value,
                        })
                      }
                      className="bg-white/50 dark:bg-gray-800/50 border-gray-200/20 dark:border-gray-700/20"
                    />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="duration">Estimated Duration</Label>
                    <Input
                      id="duration"
                      value={newService.estimated_duration}
                      onChange={(e) =>
                        setNewService({
                          ...newService,
                          estimated_duration: e.target.value,
                        })
                      }
                      placeholder="e.g., 30 minutes"
                      className="bg-white/50 dark:bg-gray-800/50 border-gray-200/20 dark:border-gray-700/20"
                    />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="hours">Operating Hours</Label>
                    <Input
                      id="hours"
                      value={newService.operating_hours}
                      onChange={(e) =>
                        setNewService({
                          ...newService,
                          operating_hours: e.target.value,
                        })
                      }
                      placeholder="e.g., 9:00 AM - 5:00 PM"
                      className="bg-white/50 dark:bg-gray-800/50 border-gray-200/20 dark:border-gray-700/20"
                    />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="requirements">
                      Requirements (comma-separated)
                    </Label>
                    <Input
                      id="requirements"
                      value={newService.requirements}
                      onChange={(e) =>
                        setNewService({
                          ...newService,
                          requirements: e.target.value,
                        })
                      }
                      placeholder="e.g., ID Card, Proof of Address"
                      className="bg-white/50 dark:bg-gray-800/50 border-gray-200/20 dark:border-gray-700/20"
                    />
                  </div>
                  <div className="flex items-center space-x-2">
                    <Switch
                      id="active"
                      checked={newService.is_active}
                      onCheckedChange={(checked) =>
                        setNewService({ ...newService, is_active: checked })
                      }
                    />
                    <Label htmlFor="active">Active Service</Label>
                  </div>
                </div>
                <div className="flex justify-end space-x-2 pt-4">
                  <Button
                    variant="outline"
                    onClick={() => setIsCreateDialogOpen(false)}
                  >
                    Cancel
                  </Button>
                  <Button
                    onClick={handleCreateService}
                    className="bg-gray-900 hover:bg-gray-800 text-white"
                  >
                    Create Service
                  </Button>
                </div>
              </DialogContent>
            </Dialog>
          )}
        </div>

        <div className="flex flex-col sm:flex-row gap-4">
          <div className="relative flex-1">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
            <Input
              placeholder="Search services..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-10 bg-white/50 dark:bg-gray-800/50 backdrop-blur-sm border-gray-200/20 dark:border-gray-700/20"
            />
          </div>
          <Select value={departmentFilter} onValueChange={setDepartmentFilter}>
            <SelectTrigger className="w-full sm:w-48 bg-white/50 dark:bg-gray-800/50 backdrop-blur-sm border-gray-200/20 dark:border-gray-700/20">
              <SelectValue placeholder="All Departments" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all">All Departments</SelectItem>
              {departments.map((dept) => (
                <SelectItem key={dept} value={dept}>
                  {dept}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
          {isAdmin && (
            <Select value={statusFilter} onValueChange={setStatusFilter}>
              <SelectTrigger className="w-full sm:w-32 bg-white/50 dark:bg-gray-800/50 backdrop-blur-sm border-gray-200/20 dark:border-gray-700/20">
                <SelectValue placeholder="Status" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Status</SelectItem>
                <SelectItem value="active">Active</SelectItem>
                <SelectItem value="inactive">Inactive</SelectItem>
              </SelectContent>
            </Select>
          )}
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredServices.map((service, index) => (
            <motion.div
              key={service.id}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: index * 0.1 }}
            >
              <Card className="bg-white/20 dark:bg-gray-800/20 backdrop-blur-sm border-gray-200/20 dark:border-gray-700/20 shadow-lg hover:shadow-xl transition-all duration-300 group">
                <CardHeader className="pb-3">
                  <div className="flex justify-between items-start">
                    <div className="flex-1">
                      <CardTitle className="text-lg font-semibold text-gray-900 dark:text-white group-hover:text-gray-700 dark:group-hover:text-gray-200 transition-colors">
                        {service.name}
                      </CardTitle>
                      <CardDescription className="text-gray-600 dark:text-gray-400 mt-1">
                        {service.description}
                      </CardDescription>
                    </div>
                    <Badge
                      variant={service.is_active ? "default" : "secondary"}
                      className={
                        service.is_active
                          ? "bg-gray-900 text-white"
                          : "bg-gray-500 text-white"
                      }
                    >
                      {service.is_active ? "Active" : "Inactive"}
                    </Badge>
                  </div>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div className="space-y-2">
                    <div className="flex items-center text-sm text-gray-600 dark:text-gray-400">
                      <MapPin className="w-4 h-4 mr-2" />
                      {service.location}
                    </div>
                    <div className="flex items-center text-sm text-gray-600 dark:text-gray-400">
                      <Clock className="w-4 h-4 mr-2" />
                      {service.estimated_duration}
                    </div>
                    <div className="flex items-center text-sm text-gray-600 dark:text-gray-400">
                      <Users className="w-4 h-4 mr-2" />
                      {service.total_bookings} bookings
                    </div>
                    <div className="flex items-center text-sm text-gray-600 dark:text-gray-400">
                      <Star className="w-4 h-4 mr-2 fill-current" />
                      {service.rating}/5.0
                    </div>
                  </div>

                  <Badge variant="outline" className="text-xs">
                    {service.department}
                  </Badge>

                  <div className="flex gap-2 pt-2">
                    <Link href={`/services/${service.id}`} className="flex-1">
                      <Button
                        variant="outline"
                        size="sm"
                        className="w-full bg-white/50 dark:bg-gray-800/50 hover:bg-white/70 dark:hover:bg-gray-700/50 border-gray-200/20 dark:border-gray-700/20"
                      >
                        <Eye className="w-4 h-4 mr-2" />
                        View Details
                      </Button>
                    </Link>

                    {isAdmin && (
                      <>
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => setEditingService(service)}
                          className="bg-white/50 dark:bg-gray-800/50 hover:bg-white/70 dark:hover:bg-gray-700/50 border-gray-200/20 dark:border-gray-700/20"
                        >
                          <Edit className="w-4 h-4" />
                        </Button>
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => handleDeleteService(service.id)}
                          className="bg-red-50/50 dark:bg-red-900/20 hover:bg-red-100/70 dark:hover:bg-red-800/30 border-red-200/20 dark:border-red-700/20 text-red-600 dark:text-red-400"
                        >
                          <Trash2 className="w-4 h-4" />
                        </Button>
                      </>
                    )}
                  </div>
                </CardContent>
              </Card>
            </motion.div>
          ))}
        </div>

        {filteredServices.length === 0 && (
          <div className="text-center py-12">
            <p className="text-gray-500 dark:text-gray-400">
              No services found matching your criteria.
            </p>
          </div>
        )}
      </div>
    </div>
  );
}
